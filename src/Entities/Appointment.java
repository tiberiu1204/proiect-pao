package src.Entities;

import src.Utils.AppointmentType;
import src.Utils.AppointmentUtils;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private Medic medic;
    private LocalDateTime date;
    private int durationMinutes;
    private AppointmentType type;
    private Disease disease;
    private double cost;
    Clinique clinique;
    int roomNumber;
    int patientId;

    public Appointment(int id, Medic medic, LocalDateTime date, int durationMinutes, AppointmentType type, Disease disease, double cost,
                       Clinique clinique, int roomNumber, int patientId) {
        this.id = id;
        this.medic = medic;
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.disease = disease;
        this.cost = cost;
        this.clinique = clinique;
        this.roomNumber = roomNumber;
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "medic=" + medic +
                ", id=" + id +
                ", date=" + date +
                ", disease=" + disease +
                ", cost=" + cost +
                ", clinique=" + clinique +
                ", roomNumber=" + roomNumber +
                ", durationMinutes=" + durationMinutes +
                ", type=" + type +
                '}';
    }


    public int getId() {
        return id;
    }

    public Medic getMedic() {
        return medic;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public AppointmentType getType() {
        return type;
    }

    public Disease getDisease() {
        return disease;
    }

    public double getCost() {
        return cost;
    }

    public Clinique getClinique() {
        return clinique;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setDate(LocalDateTime newDate) {
        this.date = newDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }
}
