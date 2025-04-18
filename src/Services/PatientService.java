package src.Services;

import src.Entities.*;
import src.Repository.PatientRepo;
import src.Utils.AppointmentType;

import java.util.ArrayList;
import java.util.Date;

public interface PatientService {
    public void makeAppointment(Appointment appointment, Patient patient);
    public void modifyAppointment(Appointment appointment, Date newDate);
    public void cancelAppointment(Appointment appointment);
    public void registerNewPatient(Patient patient, PatientRepo repo);
    public ArrayList<Appointment> getAllAppointments(Patient patient);
}
