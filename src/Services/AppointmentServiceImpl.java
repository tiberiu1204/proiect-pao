package src.Services;

import src.Config.DatabaseConfiguration;
import src.Entities.Appointment;
import src.Entities.Clinique;
import src.Entities.Disease;
import src.Entities.Medic;
import src.Utils.AppointmentType;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppointmentServiceImpl implements AppointmentService {
    private final DatabaseConfiguration db;
    public AppointmentServiceImpl(DatabaseConfiguration db) {
        this.db = db;
    }
    public Appointment createAppointmentWithMedic(Medic medic, LocalDateTime date, int durationMinutes, AppointmentType type,
                                                  Disease disease, double cost, Clinique clinique, int roomNumber, int patientId) throws SQLException, IOException {
        AuditService.log("createAppointmentWithMedic");
        MedicService medicService = new MedicServiceImpl(db);
        if(medicService.checkAvailability(medic, durationMinutes, date)) {
            Appointment appt = new Appointment(-1, medic, date, durationMinutes, type, disease, cost, clinique, roomNumber, patientId);
            return appt;
        }
        return null;
    }
}
