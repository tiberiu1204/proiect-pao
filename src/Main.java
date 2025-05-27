package src;

import src.Config.DatabaseConfiguration;
import src.Constants.Constants;
import src.Entities.*;
import src.Repositories.DiseaseRepo;
import src.Repositories.MedicRepo;
import src.Repositories.PatientRepo;
import src.Services.*;
import src.Utils.AppointmentType;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) throws SQLException {
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
        for(Appointment appointment : medicService.getAllAppointments(medic, patientRepo)) {
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
    }
}