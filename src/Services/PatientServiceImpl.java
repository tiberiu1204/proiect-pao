package src.Services;

import src.Entities.Appointment;
import src.Entities.Patient;
import src.Repository.AppointmentRepo;
import src.Repository.PatientRepo;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Date;

public class PatientServiceImpl implements PatientService{
    @Override
    public void makeAppointment(Appointment appointment, Patient patient) {
        patient.addAppointment(appointment);
    }

    @Override
    public void modifyAppointment(Appointment appointment, Date newDate) {
        AppointmentRepo appointmentRepo = new AppointmentRepo();
        ArrayList<Appointment> appointments = appointmentRepo.getAppointments();
        for(Appointment currentAppointment : appointments) {
            if(appointment.getId() == currentAppointment.getId()) {
                MedicService medicService = new MedicServiceImpl();
                if(medicService.checkAvailability(appointment.getMedic(), appointment.getDurationMinutes(), newDate)) {
                    appointment.setDate(newDate);
                }
                else {
                    System.out.println("Medic " + appointment.getMedic() + " is not available for selected time frame");
                }
                return;
            }
        }
    }

    @Override
    public void cancelAppointment(Appointment appointment) {

    }

    @Override
    public void registerNewPatient(Patient patient, PatientRepo repo) {

    }

    @Override
    public ArrayList<Appointment> getAllAppointments(Patient patient) {
        return null;
    }
}
