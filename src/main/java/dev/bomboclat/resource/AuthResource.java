package dev.bomboclat.resource;

import dev.bomboclat.model.LoginRequest;
import dev.bomboclat.model.LoginResponse;
import dev.bomboclat.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    /**
     * Login endpoint that authenticates users and returns JWT tokens
     *
     * @param loginRequest The login request containing email and password
     * @return Response with JWT token if authentication is successful, 401 Unauthorized otherwise
     */
    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<String> token = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        
        return token
                .map(t -> Response.ok(new LoginResponse(t)).build())
                .orElse(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}