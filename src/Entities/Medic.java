package src.Entities;

import src.Utils.Specialization;

import java.util.Date;

public class Medic extends Person {

    private Specialization specialization;
    private int yearsOfExperience;
    Calendar calendar;

    public Medic(String firstName, String lastName, int age, Date birth, String phoneNumber, String email,
                 String address, Specialization specialization, int yearsOfExperience, Calendar calendar) {
        super(firstName, lastName, age, birth, phoneNumber, email, address);
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
    }
}
