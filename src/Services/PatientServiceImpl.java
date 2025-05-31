package src.Services;

import src.Config.DatabaseConfiguration;
import src.Entities.*;
import src.Repositories.*;
import src.Utils.AppointmentType;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class PatientServiceImpl implements PatientService{
    private final DatabaseConfiguration db;
    private final AppointmentRepo appointmentRepo;
    private final MedicServiceImpl medicService;
    private final PatientRepo patientRepo;
    private final MedFileRepo medFileRepo;
    private final PersonRepo personRepo;
    public PatientServiceImpl(DatabaseConfiguration db) {
        this.db = db;
        appointmentRepo = new AppointmentRepo(db);
        medicService = new MedicServiceImpl(db);
        patientRepo = new PatientRepo(db);
        medFileRepo = new MedFileRepo(db);
        personRepo = new PersonRepo(db);
    }

    @Override
    public Appointment makeAppointment(Medic medic, Patient patient, LocalDateTime date, int durationMinutes, AppointmentType type, Disease disease, double cost, Clinique clinique, int roomNumber) throws SQLException, IOException {
        AuditService.log("PatientService:makeAppointment");
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
    public Appointment modifyAppointment(Patient patient, int appointmentId, LocalDateTime newDate) throws SQLException, IOException {
        AuditService.log("PatientService:modifyAppointment");
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
    public void cancelAppointment(Patient patient, int appointmentId) throws SQLException, IOException {
        AuditService.log("PatientService:cancelAppointment");
        MedFile medFile = patient.getMedFile();
        ArrayList<Appointment> appointmentHistory = medFile.getAppointmentHistory();
        for(Appointment appointment : appointmentHistory) {
            if(appointment.getId() == appointmentId) {
                appointmentHistory.remove(appointment);
                medicService.removeAppointment(appointment.getMedic(), appointment);
                appointmentRepo.delete(appointmentId);
                return;
            }
        }
    }

    @Override
    public ArrayList<Appointment> getAllAppointments(Patient patient) throws IOException {
        AuditService.log("PatientService:getAllAppointments");
        MedFile medFile = patient.getMedFile();
        return medFile.getAppointmentHistory();
    }

    @Override
    public Patient createPatient(String firstName, String lastName, int age, Date birth, String phoneNumber, String email, String address, boolean insured, MedFile medFile) throws SQLException {
        Patient patient = new Patient(-1, firstName, lastName, age, birth, phoneNumber, email, address, insured, medFile);
        personRepo.create(patient);
        patientRepo.create(patient);
        medFileRepo.create(patient.getId(), medFile);
        return patient;
    }

    @Override
    public Patient updatePatient(Patient patient, int age, boolean insured) throws SQLException {
        patient.setAge(age);
        patient.setInsured(insured);
        patientRepo.update(patient);
        personRepo.update(patient);
        return patient;
    }

    @Override
    public void deletePatient(Patient patient) throws SQLException, IOException {
        ArrayList<Appointment> appointments = this.getAllAppointments(patient);
        for(Appointment appt : appointments) {
            this.cancelAppointment(patient, appt.getId());
        }
        medFileRepo.delete(patient.getMedFile().getId());
        patientRepo.delete(patient.getId());
    }
}
