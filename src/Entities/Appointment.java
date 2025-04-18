package src.Entities;

import src.Utils.AppointmentType;
import src.Utils.AppointmentUtils;
import src.Utils.DiseaseType;

import java.util.Date;

public class Appointment {
    private int id;
    private Medic medic;
    private Date date;
    private int durationMinutes;
    private AppointmentType type;
    private Disease disease;
    private double cost;
    Clinique clinique;
    int roomNumber;

    public Appointment(Medic medic, Date date, int durationMinutes, AppointmentType type, Disease disease, double cost,
                       Clinique clinique, int roomNumber) {
        this.id = AppointmentUtils.currentId;
        AppointmentUtils.currentId++;
        this.medic = medic;
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.disease = disease;
        this.cost = cost;
        this.clinique = clinique;
        this.roomNumber = roomNumber;
    }
}
