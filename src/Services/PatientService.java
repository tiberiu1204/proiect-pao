package src.Services;

import src.Entities.*;
import src.Utils.AppointmentType;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface PatientService {
    public Appointment makeAppointment(Medic medic, Patient patient, LocalDateTime date, int durationMinutes,
                                AppointmentType type, Disease disease, double cost, Clinique clinique, int roomNumber) throws SQLException;
    public Appointment modifyAppointment(Patient patient, int appointmentId, LocalDateTime newDate) throws SQLException;
    public void cancelAppointment(Patient patient, int appointmentId) throws SQLException;
    public ArrayList<Appointment> getAllAppointments(Patient patient);
}
