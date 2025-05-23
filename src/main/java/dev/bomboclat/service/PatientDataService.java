package dev.bomboclat.service;

import dev.bomboclat.model.Patient;
import dev.bomboclat.model.PatientData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PatientDataService {

    /**
     * Load patient data from person-data.json
     *
     * @return List of patient data
     */
    private List<PatientData> loadPatientData() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("person-data.json");
            if (inputStream == null) {
                throw new RuntimeException("Could not find person-data.json");
            }
            
            Jsonb jsonb = JsonbBuilder.create();
            PatientData[] patientData = jsonb.fromJson(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8),
                    PatientData[].class
            );
            return Arrays.asList(patientData);
        } catch (Exception e) {
            throw new RuntimeException("Error loading patient data", e);
        }
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

    /**
     * Get patient data for a specific user email
     *
     * @param email The email of the user
     * @return Optional containing the patient data if found, empty otherwise
     */
    public Optional<PatientData> getPatientDataByEmail(String email) {
        // First, find the patient with the given email to get their ID
        List<Patient> patients = loadPatients();
        Optional<Patient> patient = patients.stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst();
        
        if (patient.isEmpty()) {
            return Optional.empty();
        }
        
        // Then, find the patient data with the matching user_id
        long userId = patient.get().getId();
        List<PatientData> patientDataList = loadPatientData();
        
        return patientDataList.stream()
                .filter(pd -> pd.getUser_id() == userId)
                .findFirst();
    }
}