package src.Services;

import src.Config.DatabaseConfiguration;
import src.Entities.*;
import src.Repositories.AppointmentRepo;
import src.Repositories.CalendarRepo;
import src.Repositories.MedicRepo;
import src.Repositories.PatientRepo;
import src.Utils.AppointmentType;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PatientServiceImpl implements PatientService{
    private final DatabaseConfiguration db;
    private final AppointmentRepo appointmentRepo;
    MedicServiceImpl medicService;
    public PatientServiceImpl(DatabaseConfiguration db) {
        this.db = db;
        appointmentRepo = new AppointmentRepo(db);
        medicService = new MedicServiceImpl(db);
    }

    @Override
    public Appointment makeAppointment(Medic medic, Patient patient, LocalDateTime date, int durationMinutes, AppointmentType type, Disease disease, double cost, Clinique clinique, int roomNumber) throws SQLException {
        AppointmentService appointmentService = new AppointmentServiceImpl(db);
        Appointment appointment = appointmentService.createAppointmentWithMedic(medic, date, durationMinutes, type,
                disease, cost, clinique, roomNumber, patient.getId());
        if(appointment != null) {
            patient.addAppointment(appointment);
            appointmentRepo.create(appointment);
            medicService.makeAppointment(medic, date, durationMinutes);
            return appointment;
        }
        else {
            return null;
        }
    }

    @Override
    public Appointment modifyAppointment(Patient patient, int appointmentId, LocalDateTime newDate) throws SQLException {
        ArrayList<Appointment> appointments = patient.getMedFile().getAppointmentHistory();
        for(Appointment currentAppointment : appointments) {
            if(appointmentId == currentAppointment.getId()) {
                MedicService medicService = new MedicServiceImpl(db);
                if(medicService.checkAvailability(currentAppointment.getMedic(),
                        currentAppointment.getDurationMinutes(), newDate)) {
                    LocalDateTime oldDate = currentAppointment.getDate();
                    currentAppointment.setDate(newDate);
                    appointmentRepo.update(currentAppointment);
                    medicService.modifyAppointment(currentAppointment.getMedic(), oldDate, newDate);
                    return currentAppointment;
                }
            }
        }
        return null;
    }

    @Override
    public void cancelAppointment(Patient patient, int appointmentId) throws SQLException {
        MedFile medFile = patient.getMedFile();
        ArrayList<Appointment> appointmentHistory = medFile.getAppointmentHistory();
        for(Appointment appointment : appointmentHistory) {
            if(appointment.getId() == appointmentId) {
                appointmentHistory.remove(appointment);
                appointmentRepo.delete(appointmentId);
                return;
            }
        }
    }

    @Override
    public ArrayList<Appointment> getAllAppointments(Patient patient) {
        MedFile medFile = patient.getMedFile();
        return medFile.getAppointmentHistory();
    }
}
