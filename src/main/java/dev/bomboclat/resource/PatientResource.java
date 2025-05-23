package dev.bomboclat.resource;

import dev.bomboclat.model.Patient;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

    private List<Patient> loadPatients() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json");
            if (inputStream == null) {
                throw new RuntimeException("Could not find data.json");
            }
            
            Jsonb jsonb = JsonbBuilder.create();
            Patient[] patients = jsonb.fromJson(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8),
                Patient[].class
            );
            return Arrays.asList(patients);
        } catch (Exception e) {
            throw new RuntimeException("Error loading patient data", e);
        }
    }

    @GET
    public List<Patient> getAllPatients() {
        return loadPatients();
    }

    @GET
    @Path("/{id}")
    public Response getPatientById(@PathParam("id") long id) {
        List<Patient> patients = loadPatients();
        Optional<Patient> patient = patients.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
        
        return patient
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}