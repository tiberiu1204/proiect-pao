package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Patient;
import src.Entities.Person;
import src.Entities.MedFile;

import java.sql.*;
import java.util.ArrayList;

public class PatientRepo {
    private final DatabaseConfiguration dbConfig;
    private final PersonRepo personRepo;
    private final MedFileRepo medFileRepo;

    public PatientRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig    = dbConfig;
        this.personRepo  = new PersonRepo(dbConfig);
        this.medFileRepo = new MedFileRepo(dbConfig);
    }

    // CREATE (person must exist already; med_file created separately)
    public void create(Patient p) throws SQLException {
        String sql = "INSERT INTO patients (id, insured) VALUES (?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setBoolean(2, p.isInsured());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<Patient> readAll() throws SQLException {
        String sql = "SELECT * FROM patients";
        ArrayList<Patient> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id      = rs.getInt("id");
                boolean in  = rs.getBoolean("insured");
                MedFile mf  = medFileRepo.readByPatientId(id);
                Person base = personRepo.readById(id);
                Patient p = new Patient(
                        id,
                        base.getFirstName(),
                        base.getLastName(),
                        base.getAge(),
                        base.getBirth(),
                        base.getPhoneNumber(),
                        (String) base.getEmail(),
                        base.getAddress(),
                        in,
                        mf
                );
                list.add(p);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY ID
    public Patient readById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                boolean in  = rs.getBoolean("insured");
                MedFile mf  = medFileRepo.readByPatientId(id);
                Person base = personRepo.readById(id);
                return new Patient(
                        id,
                        base.getFirstName(),
                        base.getLastName(),
                        base.getAge(),
                        base.getBirth(),
                        base.getPhoneNumber(),
                        (String) base.getEmail(),
                        base.getAddress(),
                        in,
                        mf
                );
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // UPDATE
    public void update(Patient p) throws SQLException {
        String sql = "UPDATE patients SET insured=? WHERE id=?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, p.isInsured());
            ps.setInt(2, p.getId());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }
}
