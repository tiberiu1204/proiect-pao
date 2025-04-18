package src.Entities;

import java.util.ArrayList;

public class MedFile {
    private ArrayList<Appointment> appointmentHistory;

    public MedFile(ArrayList<Appointment> appointmentHistory) {
        this.appointmentHistory = appointmentHistory;
    }
}
