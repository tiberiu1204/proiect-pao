package src.Services;

import src.Config.DatabaseConfiguration;
import src.Entities.*;
import src.Entities.Calendar;
import src.Repositories.MedicRepo;
import src.Repositories.PatientRepo;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MedicServiceImpl implements MedicService {
    private final DatabaseConfiguration db;
    public MedicServiceImpl(DatabaseConfiguration db) {
        this.db = db;
    }
    @Override
    public boolean checkAvailability(Medic medic, int durationMinutes, LocalDateTime date) {
        return medic.getCalendar().dateAvailable(date, durationMinutes);
    }

    @Override
    public ArrayList<Patient> getAllPatients(Medic medic, PatientRepo patientRepo) throws SQLException {
        ArrayList<Patient> queryResult = new ArrayList<>();
        ArrayList<Patient> patients = patientRepo.readAll();
        for(Patient patient : patients) {
            MedFile medFile = patient.getMedFile();
            for(Appointment appointment : medFile.getAppointmentHistory()) {
                Medic attendingMedic = appointment.getMedic();
                if(attendingMedic.getEmail() == medic.getEmail()) {
                    queryResult.add(patient);
                    break;
                }
            }
        }
        Collections.sort(queryResult);
        return queryResult;
    }

    @Override
    public ArrayList<Appointment> getAllAppointments(Medic medic, PatientRepo patientRepo) throws SQLException {
        ArrayList<Appointment> queryResult = new ArrayList<>();
        for(Patient patient : patientRepo.readAll()) {
            ArrayList<Appointment> appointmentHistory = patient.getMedFile().getAppointmentHistory();
            for(Appointment appointment : appointmentHistory) {
                if(appointment.getMedic().getEmail() == medic.getEmail()) {
                    queryResult.add(appointment);
                }
            }
        }
        return queryResult;
    }

    @Override
    public ArrayList<Medic> getAllMedics() throws SQLException {
        MedicRepo medicRepo = new MedicRepo(db);
        ArrayList<Medic> medics = medicRepo.readAll();
        Collections.sort(medics);
        return medics;
    }

    @Override
    public LocalDateTime getFirstAvailableTimeFrame(Medic medic, int durationMinutes) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Calendar calendar = medic.getCalendar();
        HashMap<LocalDateTime, Integer> appointedDates = calendar.getAppointedDates();
        ArrayList<Integer> freeDaysThisYear = calendar.getFreeDaysThisYear();
        int startHour = calendar.getStartHour();
        int endHour = calendar.getEndHour();

        TreeMap<LocalDateTime, Integer> sortedAppointments = new TreeMap<>(appointedDates);

        LocalDateTime pointer = now;

        while (true) {

            DayOfWeek day = pointer.getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                pointer = pointer.plusDays(1).withHour(startHour).withMinute(0);
                continue;
            }

            int dayOfYear = pointer.getDayOfYear();
            if (freeDaysThisYear.contains(dayOfYear)) {
                pointer = pointer.plusDays(1).withHour(startHour).withMinute(0);
                continue;
            }

            if (pointer.getHour() < startHour) {
                pointer = pointer.withHour(startHour).withMinute(0);
            }

            if (pointer.getHour() >= endHour ||
                    (pointer.getHour() == endHour && pointer.getMinute() > 0)) {
                pointer = pointer.plusDays(1).withHour(startHour).withMinute(0);
                continue;
            }

            LocalDateTime slotEnd = pointer.plusMinutes(durationMinutes);

            if (slotEnd.getHour() > endHour ||
                    (slotEnd.getHour() == endHour && slotEnd.getMinute() > 0)) {
                pointer = pointer.plusDays(1).withHour(startHour).withMinute(0);
                continue;
            }

            boolean hasConflict = false;

            for (Map.Entry<LocalDateTime, Integer> entry : sortedAppointments.entrySet()) {
                LocalDateTime apptStart = entry.getKey();
                LocalDateTime apptEnd = apptStart.plusMinutes(entry.getValue());

                if (apptStart.isAfter(slotEnd)) {
                    break;
                }

                if (!(slotEnd.isBefore(apptStart) || pointer.isAfter(apptEnd.minusMinutes(1)))) {
                    pointer = apptEnd.truncatedTo(ChronoUnit.MINUTES);
                    hasConflict = true;
                    break;
                }
            }

            if (!hasConflict) {
                return pointer;
            }
        }
    }
}
