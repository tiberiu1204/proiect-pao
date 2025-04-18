package src.Entities;

import java.util.ArrayList;
import java.util.Date;

public class MedFile {
    private ArrayList<Appointment> appointmentHistory;
    private Date registrationDate;

    public MedFile(ArrayList<Appointment> appointmentHistory, Date registrationDate) {
        this.appointmentHistory = appointmentHistory;
        this.registrationDate = registrationDate;
    }
}
