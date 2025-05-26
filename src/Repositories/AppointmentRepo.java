package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Appointment;
import src.Entities.Medic;
import src.Entities.Patient;
import src.Entities.Disease;
import src.Entities.Clinique;
import src.Utils.AppointmentType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AppointmentRepo {
    private final DatabaseConfiguration dbConfig;
    private final MedicRepo medicRepo;
    private final PatientRepo patientRepo;
    private final DiseaseRepo diseaseRepo;
    private final CliniqueRepo cliniqueRepo;

    public AppointmentRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig     = dbConfig;
        this.medicRepo    = new MedicRepo(dbConfig);
        this.patientRepo  = new PatientRepo(dbConfig);
        this.diseaseRepo  = new DiseaseRepo(dbConfig);
        this.cliniqueRepo = new CliniqueRepo(dbConfig);
    }

    // CREATE
    public void create(Appointment a) throws SQLException {
        String sql = "INSERT INTO appointments "
                + "(medic_id, patient_id, date_time, duration_minutes, type, "
                + "disease_id, cost, clinique_id, room_number) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getMedic().getId());
            if (a.getPatient() != null) ps.setInt(2, a.getPatient().getId());
            else                         ps.setNull(2, Types.INTEGER);
            ps.setTimestamp(3, Timestamp.valueOf(a.getDate()));
            ps.setInt(4, a.getDurationMinutes());
            ps.setString(5, a.getType().name());
            ps.setInt(6, a.getDisease().getId());
            ps.setDouble(7, a.getCost());
            ps.setInt(8, a.getClinique().getId());
            ps.setInt(9, a.getRoomNumber());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setId(keys.getInt(1));
                }
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<Appointment> readAll() throws SQLException {
        String sql = "SELECT * FROM appointments";
        ArrayList<Appointment> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id        = rs.getInt("id");
                Medic medic   = medicRepo.readById(rs.getInt("medic_id"));
                Patient pat   = patientRepo.readById(rs.getInt("patient_id"));
                LocalDateTime dt   = rs.getTimestamp("date_time").toLocalDateTime();
                int duration       = rs.getInt("duration_minutes");
                AppointmentType t  = AppointmentType.valueOf(rs.getString("type"));
                Disease disease    = diseaseRepo.readById(rs.getInt("disease_id"));
                double cost        = rs.getDouble("cost");
                Clinique cliq      = cliniqueRepo.readById(rs.getInt("clinique_id"));
                int roomNum        = rs.getInt("room_number");

                Appointment a = new Appointment(
                        id, medic, dt, duration, t, disease, cost, cliq, roomNum
                );
                if (pat != null) a.setPatient(pat);
                list.add(a);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY ID
    public Appointment readById(int id) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Medic medic   = medicRepo.readById(rs.getInt("medic_id"));
                Patient pat   = patientRepo.readById(rs.getInt("patient_id"));
                LocalDateTime dt   = rs.getTimestamp("date_time").toLocalDateTime();
                int duration       = rs.getInt("duration_minutes");
                AppointmentType t  = AppointmentType.valueOf(rs.getString("type"));
                Disease disease    = diseaseRepo.readById(rs.getInt("disease_id"));
                double cost        = rs.getDouble("cost");
                Clinique cliq      = cliniqueRepo.readById(rs.getInt("clinique_id"));
                int roomNum        = rs.getInt("room_number");

                Appointment a = new Appointment(
                        id, medic, dt, duration, t, disease, cost, cliq, roomNum
                );
                if (pat != null) a.setPatient(pat);
                return a;
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // UPDATE
    public void update(Appointment a) throws SQLException {
        String sql = "UPDATE appointments SET "
                + "medic_id=?, patient_id=?, date_time=?, duration_minutes=?, type=?, "
                + "disease_id=?, cost=?, clinique_id=?, room_number=? "
                + "WHERE id=?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getMedic().getId());
            if (a.getPatient() != null) ps.setInt(2, a.getPatient().getId());
            else                         ps.setNull(2, Types.INTEGER);
            ps.setTimestamp(3, Timestamp.valueOf(a.getDate()));
            ps.setInt(4, a.getDurationMinutes());
            ps.setString(5, a.getType().name());
            ps.setInt(6, a.getDisease().getId());
            ps.setDouble(7, a.getCost());
            ps.setInt(8, a.getClinique().getId());
            ps.setInt(9, a.getRoomNumber());
            ps.setInt(10, a.getId());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // HELPER: fetch all by patient_id
    public ArrayList<Appointment> readByPatientId(int patientId) throws SQLException {
        String sql = "SELECT id FROM appointments WHERE patient_id = ?";
        ArrayList<Appointment> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readById(rs.getInt("id")));
                }
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }
}
