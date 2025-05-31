package src.Services;

import src.Entities.*;
import src.Utils.AppointmentType;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public interface PatientService {
    public Appointment makeAppointment(Medic medic, Patient patient, LocalDateTime date, int durationMinutes,
                                AppointmentType type, Disease disease, double cost, Clinique clinique, int roomNumber) throws SQLException, IOException;
    public Appointment modifyAppointment(Patient patient, int appointmentId, LocalDateTime newDate) throws SQLException, IOException;
    public void cancelAppointment(Patient patient, int appointmentId) throws SQLException, IOException;
    public ArrayList<Appointment> getAllAppointments(Patient patient) throws IOException;
    public Patient createPatient(String firstName, String lastName, int age, Date birth, String phoneNumber, String email,
                         String address, boolean insured, MedFile medFile) throws SQLException;
    public Patient updatePatient(Patient patient, int age, boolean insured) throws SQLException;
    public void deletePatient(Patient patient) throws SQLException, IOException;
}
