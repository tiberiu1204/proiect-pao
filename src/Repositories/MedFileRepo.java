package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.MedFile;
import src.Entities.Appointment;

import java.sql.*;
import java.util.ArrayList;

public class MedFileRepo {
    private final DatabaseConfiguration dbConfig;
    private final AppointmentRepo apptRepo;

    public MedFileRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig = dbConfig;
        this.apptRepo = new AppointmentRepo(dbConfig);
    }

    // CREATE
    public void create(int patientId, MedFile mf) throws SQLException {
        String sql = "INSERT INTO med_files (patient_id, registration_date) VALUES (?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, patientId);
            ps.setDate(2, new java.sql.Date(mf.getRegistrationDate().getTime()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    mf.setId(keys.getInt(1));
                }
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<MedFile> readAll() throws SQLException {
        String sql = "SELECT * FROM med_files";
        ArrayList<MedFile> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int       id         = rs.getInt("id");
                int       pid        = rs.getInt("patient_id");
                java.util.Date reg   = rs.getDate("registration_date");
                ArrayList<Appointment> history = apptRepo.readByPatientId(pid);
                MedFile mf = new MedFile(id, history, reg);
                list.add(mf);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY ID
    public MedFile readById(int id) throws SQLException {
        String sql = "SELECT * FROM med_files WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                int       pid     = rs.getInt("patient_id");
                java.util.Date reg= rs.getDate("registration_date");
                ArrayList<Appointment> history = apptRepo.readByPatientId(pid);
                return new MedFile(id, history, reg);
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ BY PATIENT_ID
    public MedFile readByPatientId(int patientId) throws SQLException {
        String sql = "SELECT * FROM med_files WHERE patient_id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                int       id    = rs.getInt("id");
                java.util.Date reg= rs.getDate("registration_date");
                ArrayList<Appointment> history = apptRepo.readByPatientId(patientId);
                return new MedFile(id, history, reg);
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // UPDATE
    public void update(MedFile mf, int patientId) throws SQLException {
        String sql = "UPDATE med_files SET registration_date=? WHERE patient_id=?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(mf.getRegistrationDate().getTime()));
            ps.setInt(2, patientId);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM med_files WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }
}
