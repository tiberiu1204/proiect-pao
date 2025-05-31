package src;

import src.Config.DatabaseConfiguration;
import src.Constants.Constants;
import src.Entities.*;
import src.Repositories.DiseaseRepo;
import src.Repositories.MedicRepo;
import src.Repositories.PatientRepo;
import src.Services.*;
import src.Utils.AppointmentType;
import src.Utils.Specialization;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        DatabaseConfiguration db = new DatabaseConfiguration(Constants.DATABASE_CREDENTIALS);
        CliniqueService cliniqueService = new CliniqueServiceImpl(db);
        for(Clinique clinique : cliniqueService.getAllCliniques()) {
            System.out.println(clinique);
        }
        MedicService medicService = new MedicServiceImpl(db);
        MedicRepo medicRepo = new MedicRepo(db);
        PatientRepo patientRepo = new PatientRepo(db);
        Medic medic = medicRepo.readAll().getFirst();
        if(medicService.checkAvailability(medic, 45, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusDays(2).minusHours(1))) {
            System.out.println("Medic " + medic + " is available.");
        }
        else {
            System.out.println("Medic " + medic + " is not available.");
        }
        System.out.println("All medics:");
        for(Medic m : medicService.getAllMedics()) {
            System.out.println(m);
        }
        System.out.println("All appointments for medic " + medic);
        for(Appointment appointment : medicService.getAllAppointments(medic)) {
            System.out.println(appointment);
        }
        System.out.println("All patients for medic " + medic );
        for(Patient patient : medicService.getAllPatients(medic, patientRepo)) {
            System.out.println(patient);
        }
        System.out.println("First available timeframe for " + medic);
        System.out.println(medicService.getFirstAvailableTimeFrame(medic, 70));
        PatientService patientService = new PatientServiceImpl(db);
        DiseaseRepo diseaseRepo = new DiseaseRepo(db);
        Disease disease = diseaseRepo.readAll().getFirst();
        Patient patient = patientRepo.readAll().getFirst();
        System.out.println(patientService.makeAppointment(medic, patient, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                45, AppointmentType.CONSULTATION, disease, 100.00, cliniqueService.getAllCliniques().getFirst(), 10));
        System.out.println(patientService.modifyAppointment(patient, 1, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
        System.out.println(patientService.getAllAppointments(patient));
        patientService.cancelAppointment(patient, 1);
        System.out.println(patientService.getAllAppointments(patient));
        Clinique newClinique = cliniqueService.createClinique("Spitalul Universitar", "Bucuresti", new ArrayList<>());
        System.out.println(newClinique);
        System.out.println(cliniqueService.updateCliniqueName(newClinique, "Spitalul Nume Creativ"));
        cliniqueService.removeClinique(newClinique);
        Medic newMedic = medicService.createMedic("Gigel", "Costel", 30, new Date(), "0712123123", "gigel.costel@gmail.com", "Bucuresti", Specialization.CARDIOLOGY, 1, new Calendar(-1, 8, 16, new ArrayList<>(), new HashMap<>()));
        System.out.println(newMedic);
        System.out.println(medicService.updateMedic(newMedic, 31, 2, medic.getSpecialization()));
        medicService.deleteMedic(medic);
        Patient newPatient = patientService.createPatient("Costel", "Gigel", 30, new Date(), "0712123124", "costel.gigel@gmail.com", "Bucuresti", false, new MedFile(-1, new ArrayList<>(), new Date()));
        System.out.println(patientService.updatePatient(newPatient, 31, true).getAge());
        patientService.deletePatient(newPatient);
    }
}