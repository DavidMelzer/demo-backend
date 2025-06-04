package dev.bomboclat.resource;

import dev.bomboclat.model.LoginRequest;
import dev.bomboclat.model.LoginResponse;
import dev.bomboclat.model.RegisterRequest;
import dev.bomboclat.service.AuthService;
import dev.bomboclat.service.UserService;
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

    @Inject
    UserService userService;

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

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        if (request == null || request.email == null || request.password == null || request.name == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            userService.register(request.email, request.password, request.name);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}