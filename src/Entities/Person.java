package src.Entities;

import java.util.Date;

public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private Date birth;
    private String phoneNumber;
    private String email;
    private String address;

    public Person(String firstName, String lastName, int age, Date birth, String phoneNumber, String email,
                  String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }
}
