package src.Services;

import src.Entities.Appointment;
import src.Entities.Clinique;
import src.Entities.Disease;
import src.Entities.Medic;
import src.Utils.AppointmentType;

import java.util.Date;

public interface AppointmentService {
    public Appointment createAppointmentWithMedic(Medic medic, Date date, int durationMinutes, AppointmentType type,
                                                  Disease disease, double cost, Clinique clinique, int roomNumber);
}
