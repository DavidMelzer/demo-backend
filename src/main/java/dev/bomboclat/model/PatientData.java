package dev.bomboclat.model;

import java.util.List;

public class PatientData {
    private long id;
    private long user_id;
    private String rheuma_type;
    private List<Medication> medication;

    // Default constructor required for JSON binding
    public PatientData() {
    }

    public PatientData(long id, long user_id, String rheuma_type, List<Medication> medication) {
        this.id = id;
        this.user_id = user_id;
        this.rheuma_type = rheuma_type;
        this.medication = medication;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getRheuma_type() {
        return rheuma_type;
    }

    public void setRheuma_type(String rheuma_type) {
        this.rheuma_type = rheuma_type;
    }

    public List<Medication> getMedication() {
        return medication;
    }

    public void setMedication(List<Medication> medication) {
        this.medication = medication;
    }
}