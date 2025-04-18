package src.Services;

import src.Entities.Appointment;
import src.Entities.MedFile;
import src.Entities.Medic;
import src.Entities.Patient;
import src.Repository.AppointmentRepo;
import src.Repository.MedicRepo;
import src.Repository.PatientRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class MedicServiceImpl implements MedicService {
    @Override
    public boolean checkAvailability(Medic medic, int durationMinutes, Date date) {
        return true;
    }

    @Override
    public void registerNewMedic(Medic medic, MedicRepo repo) {
        repo.addMedic(medic);
    }

    @Override
    public ArrayList<Patient> getAllPatients(Medic medic) {
        ArrayList<Patient> queryResult = new ArrayList<>();
        PatientRepo patientRepo = new PatientRepo();
        ArrayList<Patient> patients = patientRepo.getPatients();
        for(Patient patient : patients) {
            MedFile medFile = patient.getMedFile();
            for(Appointment appointment : medFile.getAppointmentHistory()) {
                Medic attendingMedic = appointment.getMedic();
                if(attendingMedic.getId() == medic.getId()) {
                    queryResult.add(patient);
                    break;
                }
            }
        }
        Collections.sort(queryResult);
        return queryResult;
    }

    @Override
    public ArrayList<Appointment> getAllAppointments(Medic medic) {
        ArrayList<Appointment> queryResult = new ArrayList<>();
        AppointmentRepo appointmentRepo = new AppointmentRepo();
        ArrayList<Appointment> appointments = appointmentRepo.getAppointments();
        for(Appointment appointment : appointments) {
            if(medic.getId() == appointment.getMedic().getId()) {
                queryResult.add(appointment);
            }
        }
        return queryResult;
    }

    @Override
    public ArrayList<Medic> getAllMedics() {
        MedicRepo medicRepo = new MedicRepo();
        ArrayList<Medic> medics = medicRepo.getMedics();
        Collections.sort(medics);
        return medics;
    }
}
