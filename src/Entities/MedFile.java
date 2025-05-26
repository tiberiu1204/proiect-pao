package src.Entities;

import java.util.ArrayList;
import java.util.Date;

public class MedFile {
    private int id;
    private ArrayList<Appointment> appointmentHistory;
    private Date registrationDate;

    public MedFile(int id, ArrayList<Appointment> appointmentHistory, Date registrationDate) {
        this.id = id;
        this.appointmentHistory = appointmentHistory;
        this.registrationDate = registrationDate;
    }

    public ArrayList<Appointment> getAppointmentHistory() {
        return this.appointmentHistory;
    }

    public void addAppointment(Appointment appointment) {
        appointmentHistory.add(appointment);
    }

    public int getId() {
        return id;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setId(int id) {
        this.id = id;
    }
}
