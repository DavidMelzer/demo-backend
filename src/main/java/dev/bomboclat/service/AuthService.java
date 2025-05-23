package dev.bomboclat.service;

import dev.bomboclat.model.Patient;
import dev.bomboclat.util.JwtUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    JwtUtil jwtUtil;

    /**
     * Authenticate a user with email and password
     *
     * @param email    The email to authenticate
     * @param password The password to authenticate
     * @return Optional containing the JWT token if authentication is successful, empty otherwise
     */
    public Optional<String> authenticate(String email, String password) {
        List<Patient> patients = loadPatients();
        
        return patients.stream()
                .filter(p -> p.getEmail().equals(email) && p.getPassword().equals(password))
                .findFirst()
                .map(p -> jwtUtil.generateToken(p.getEmail()));
    }

    /**
     * Load patients from data.json
     *
     * @return List of patients
     */
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
}