package dev.bomboclat.model;

public class Medication {
    private String status;
    private String prescribed_at;
    private String stopped_at;
    private String name;
    private String dose;
    private int morning;
    private int afternoon;
    private int evening;
    private int night;

    // Default constructor required for JSON binding
    public Medication() {
    }

    public Medication(String status, String prescribed_at, String stopped_at, String name, String dose,
                     int morning, int afternoon, int evening, int night) {
        this.status = status;
        this.prescribed_at = prescribed_at;
        this.stopped_at = stopped_at;
        this.name = name;
        this.dose = dose;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrescribed_at() {
        return prescribed_at;
    }

    public void setPrescribed_at(String prescribed_at) {
        this.prescribed_at = prescribed_at;
    }

    public String getStopped_at() {
        return stopped_at;
    }

    public void setStopped_at(String stopped_at) {
        this.stopped_at = stopped_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public int getMorning() {
        return morning;
    }

    public void setMorning(int morning) {
        this.morning = morning;
    }

    public int getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(int afternoon) {
        this.afternoon = afternoon;
    }

    public int getEvening() {
        return evening;
    }

    public void setEvening(int evening) {
        this.evening = evening;
    }

    public int getNight() {
        return night;
    }

    public void setNight(int night) {
        this.night = night;
    }
}