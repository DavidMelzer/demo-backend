package dev.bomboclat.resource;

import dev.bomboclat.model.SymptomEntry;
import dev.bomboclat.model.User;
import dev.bomboclat.service.SymptomService;
import dev.bomboclat.util.JwtUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/symptoms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SymptomResource {

    @Inject
    JwtUtil jwtUtil;

    @Inject
    SymptomService symptomService;


    private Optional<User> getUserFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        if (email == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(User.find("email", email).firstResult());
    }

    @POST
    public Response create(@HeaderParam("Authorization") String auth, SymptomEntry entry) {
        Optional<User> userOpt = getUserFromHeader(auth);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        SymptomEntry saved = symptomService.createEntry(userOpt.get(), entry);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @GET
    public Response list(@HeaderParam("Authorization") String auth) {
        Optional<User> userOpt = getUserFromHeader(auth);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<SymptomEntry> entries = symptomService.listEntries(userOpt.get());
        return Response.ok(entries).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@HeaderParam("Authorization") String auth, @PathParam("id") UUID id, SymptomEntry entry) {
        Optional<User> userOpt = getUserFromHeader(auth);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        SymptomEntry updated = symptomService.updateEntry(id, entry, userOpt.get());
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String auth, @PathParam("id") UUID id) {
        Optional<User> userOpt = getUserFromHeader(auth);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        boolean deleted = symptomService.softDelete(id, userOpt.get());
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
