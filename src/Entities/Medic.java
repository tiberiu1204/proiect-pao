package src.Entities;

import src.Utils.Specialization;

import java.time.LocalDateTime;
import java.util.Date;

public class Medic extends Person {

    private Specialization specialization;
    private int yearsOfExperience;
    private Calendar calendar;

    public Medic(int id, String firstName, String lastName, int age, Date birth, String phoneNumber, String email,
                 String address, Specialization specialization, int yearsOfExperience, Calendar calendar) {
        super(id, firstName, lastName, age, birth, phoneNumber, email, address);
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.calendar = calendar;
    }

    public void makeAppointment(LocalDateTime date, int durationMinutes) {
        calendar.makeAppointment(date, durationMinutes);
    }

    public void modifyAppointment(LocalDateTime oldDate, LocalDateTime newDate) {
        calendar.modifyCalendar(oldDate, newDate);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + yearsOfExperience +
                " years of experience, specialized in " + specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void removeAppointment(Appointment appointment) {
        calendar.removeAppointment(appointment.getDate());
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
}
