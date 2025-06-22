package dev.bomboclat.resource;

import dev.bomboclat.model.PatientData;
import dev.bomboclat.service.PatientDataService;
import dev.bomboclat.util.JwtUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.util.Optional;

@Path("/patient-data")
@Produces(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "bearerAuth")
public class PatientDataResource {

    @Inject
    PatientDataService patientDataService;

    @Inject
    JwtUtil jwtUtil;

    /**
     * Get patient data for the authenticated user
     *
     * @param authHeader The Authorization header containing the JWT token
     * @return Response with patient data if authentication is successful, appropriate error response otherwise
     */
    @GET
    public Response getPatientData(@HeaderParam("Authorization") String authHeader) {
        // Token validation is now handled by the JwtAuthFilter
        // Extract the token from the Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Missing or invalid Authorization header")
                    .build();
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        // Extract the email from the token
        String email = jwtUtil.extractEmail(token);
        if (email == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired token")
                    .build();
        }

        // Get patient data for the authenticated user
        Optional<PatientData> patientData = patientDataService.getPatientDataByEmail(email);

        return patientData
                .map(data -> Response.ok(data).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("No patient data found for the authenticated user")
                        .build());
    }
}
