package src.Services;

import src.Entities.Appointment;
import src.Entities.Medic;
import src.Entities.Patient;
import src.Repositories.PatientRepo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface MedicService {
    public boolean checkAvailability(Medic medic, int durationMinutes, LocalDateTime date);
    public ArrayList<Patient> getAllPatients(Medic medic, PatientRepo patientRepo) throws SQLException;
    public ArrayList<Appointment> getAllAppointments(Medic medic, PatientRepo patientRepo) throws SQLException;
    public ArrayList<Medic> getAllMedics() throws SQLException;
    public LocalDateTime getFirstAvailableTimeFrame(Medic medic, int durationMinutes);
    public void makeAppointment(Medic medic, LocalDateTime date, int durationMinutes) throws SQLException;
    public void modifyAppointment(Medic medic, LocalDateTime oldDate, LocalDateTime newDate) throws SQLException;
    public void removeAppointment(Medic medic, Appointment appointment) throws SQLException;
}
