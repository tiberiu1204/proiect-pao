package src.Entities;
import src.Utils.PersonUtils;

import java.util.Date;
import java.util.Objects;

public class Person implements Comparable<Person>{
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private Date birth;
    private String phoneNumber;
    private String email;
    private String address;

    public Person(int id, String firstName, String lastName, int age, Date birth, String phoneNumber, String email,
                  String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(Person other) {
        if(Objects.equals(this.firstName, other.lastName)) {
           return this.lastName.compareTo(other.lastName);
        }
        else {
            return this.firstName.compareTo(other.firstName);
        }
    }

    public int getId() {
        return this.id;
    }

    public Object getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public Date getBirth() {
        return birth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
