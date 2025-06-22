package dev.bomboclat.filter;

import dev.bomboclat.util.JwtUtil;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    @Inject
    JwtUtil jwtUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Skip authentication for paths that don't require it
        String path = requestContext.getUriInfo().getPath();
        if (!path.startsWith("patient-data") && !path.startsWith("symptoms")) {
            return;
        }

        // Get the Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("[DEBUG_LOG] Missing or invalid Authorization header: " + authorizationHeader);
            abortWithUnauthorized(requestContext, "Missing or invalid Authorization header");
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        System.out.println("[DEBUG_LOG] Extracted token in filter: " + token);

        // Validate the token
        if (!jwtUtil.validateToken(token)) {
            System.out.println("[DEBUG_LOG] Invalid token in filter");
            abortWithUnauthorized(requestContext, "Invalid or expired token");
            return;
        }

        // Extract the email from the token
        String email = jwtUtil.extractEmail(token);
        if (email == null) {
            System.out.println("[DEBUG_LOG] Could not extract email from token in filter");
            abortWithUnauthorized(requestContext, "Invalid token: could not extract email");
            return;
        }

        System.out.println("[DEBUG_LOG] Token validated successfully in filter for email: " + email);
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(message)
                        .build());
    }
}