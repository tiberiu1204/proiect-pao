package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Calendar;
import src.Entities.Medic;
import src.Entities.Person;
import src.Utils.Specialization;

import java.sql.*;
import java.util.ArrayList;

public class MedicRepo {
    private final DatabaseConfiguration dbConfig;
    private final PersonRepo personRepo;
    private final CalendarRepo calendarRepo;

    public MedicRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig   = dbConfig;
        this.personRepo = new PersonRepo(dbConfig);
        this.calendarRepo = new CalendarRepo(dbConfig);
    }

    // CREATE (person must exist already)
    public void create(Medic m) throws SQLException {
        String sql = "INSERT INTO medics (id, specialization, years_experience) VALUES (?,?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getId());
            ps.setString(2, m.getSpecialization().name());
            ps.setInt(3, m.getYearsOfExperience());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<Medic> readAll() throws SQLException {
        String sql = "SELECT * FROM medics";
        ArrayList<Medic> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id     = rs.getInt("id");
                String s   = rs.getString("specialization");
                int years  = rs.getInt("years_experience");
                int calendarId = rs.getInt("calendar_id");

                // load Person data
                Person base = personRepo.readById(id);
                // load calendar data
                Calendar calendar = calendarRepo.readById(calendarId);
                Medic m = new Medic(
                        id,
                        base.getFirstName(),
                        base.getLastName(),
                        base.getAge(),
                        base.getBirth(),
                        base.getPhoneNumber(),
                        (String) base.getEmail(),
                        base.getAddress(),
                        Specialization.valueOf(s),
                        years,
                        calendar
                );
                list.add(m);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY ID
    public Medic readById(int id) throws SQLException {
        String sql = "SELECT * FROM medics WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Person base = personRepo.readById(id);
                int calendarId = rs.getInt("calendar_id");
                Calendar calendar = calendarRepo.readById(calendarId);
                return new Medic(
                        id,
                        base.getFirstName(),
                        base.getLastName(),
                        base.getAge(),
                        base.getBirth(),
                        base.getPhoneNumber(),
                        (String) base.getEmail(), base.getAddress(),
                        Specialization.valueOf(rs.getString("specialization")),
                        rs.getInt("years_experience"),
                        calendar
                );
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // UPDATE
    public void update(Medic m) throws SQLException {
        String sql = "UPDATE medics SET specialization=?, years_experience=? WHERE id=?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getSpecialization().name());
            ps.setInt(2, m.getYearsOfExperience());
            ps.setInt(3, m.getId());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM medics WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }
}
