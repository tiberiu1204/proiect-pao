package src.Entities;

import src.Utils.AppointmentType;
import src.Utils.AppointmentUtils;
import src.Utils.DiseaseType;

import java.util.Date;

public class Appointment {
    private int id;
    private Medic medic;
    private Patient patient;
    private Date date;
    private int durationMinutes;
    private AppointmentType type;
    private Disease disease;
    private double cost;

    public Appointment(Medic medic, Patient patient, Date date, int durationMinutes, AppointmentType type,
                       Disease disease, double cost) {
        this.id = AppointmentUtils.currentId;
        AppointmentUtils.currentId++;
        this.medic = medic;
        this.patient = patient;
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.disease = disease;
        this.cost = cost;
    }
}
