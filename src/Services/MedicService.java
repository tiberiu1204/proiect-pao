package src.Services;

import src.Entities.Appointment;
import src.Entities.Medic;
import src.Entities.Patient;
import src.Repository.MedicRepo;

import java.util.ArrayList;
import java.util.Date;

public interface MedicService {
    boolean checkAvailability(Medic medic, int durationMinutes, Date date);
    public void registerNewMedic(Medic medic, MedicRepo repo);
    public ArrayList<Patient> getAllPatients(Medic medic);
    public ArrayList<Appointment> getAllAppointments(Medic medic);
    public ArrayList<Medic> getAllMedics();
}
