package dev.bomboclat.resource;

import dev.bomboclat.service.AiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/chat")
public class AiResource {

    @Inject
    AiService assistant;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/basic")
    public String chat(@QueryParam("message") String message) {
        return assistant.chat(message);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/habits")
    public Response suggestHabits(List<String> existingHabits) {
        List<String> habits = assistant.suggestHabits(existingHabits);
        // Trim each string in the response to remove extra spaces
        List<String> trimmedHabits = habits.stream()
                .map(String::trim)
                .toList();
        return Response.ok(trimmedHabits)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
