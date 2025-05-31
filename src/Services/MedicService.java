package src.Services;

import src.Entities.Appointment;
import src.Entities.Calendar;
import src.Entities.Medic;
import src.Entities.Patient;
import src.Repositories.PatientRepo;
import src.Utils.Specialization;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public interface MedicService {
    public boolean checkAvailability(Medic medic, int durationMinutes, LocalDateTime date) throws IOException;
    public ArrayList<Patient> getAllPatients(Medic medic, PatientRepo patientRepo) throws SQLException, IOException;
    public ArrayList<Appointment> getAllAppointments(Medic medic) throws SQLException, IOException;
    public ArrayList<Medic> getAllMedics() throws SQLException, IOException;
    public LocalDateTime getFirstAvailableTimeFrame(Medic medic, int durationMinutes) throws IOException;
    public void makeAppointment(Medic medic, LocalDateTime date, int durationMinutes) throws SQLException;
    public void modifyAppointment(Medic medic, LocalDateTime oldDate, LocalDateTime newDate) throws SQLException;
    public void removeAppointment(Medic medic, Appointment appointment) throws SQLException;
    public Medic createMedic(String firstName, String lastName, int age, Date birth, String phoneNumber, String email,
                            String address, Specialization specialization, int yearsOfExperience, Calendar calendar) throws SQLException;
    public Medic updateMedic(Medic medic, int age, int yearsOfExperience, Specialization specialization) throws SQLException;
    public void deleteMedic(Medic medic) throws SQLException, IOException;
}
