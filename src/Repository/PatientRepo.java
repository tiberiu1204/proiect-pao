
package src.Repository;

import src.Entities.Patient;

import java.util.ArrayList;

public class PatientRepo {
    private ArrayList<Patient> patients;

    public PatientRepo() {
        this.patients = new ArrayList<>();
        this.fetchPatients();
    }

    private void fetchPatients() {

    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }
}
    