package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Clinique;
import src.Entities.Room;

import java.sql.*;
import java.util.ArrayList;

public class CliniqueRepo {
    private final DatabaseConfiguration dbConfig;
    private final RoomRepo roomRepo;

    public CliniqueRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig = dbConfig;
        this.roomRepo = new RoomRepo(dbConfig);
    }

    // CREATE
    public void create(Clinique c) throws SQLException {
        String sql = "INSERT INTO cliniques (name, address) VALUES (?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getAddress());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<Clinique> readAll() throws SQLException {
        String sql = "SELECT * FROM cliniques";
        ArrayList<Clinique> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id       = rs.getInt("id");
                String name  = rs.getString("name");
                String addr  = rs.getString("address");
                ArrayList<Room> rooms = roomRepo.readByCliniqueId(id);
                Clinique c = new Clinique(id, name, addr, rooms);
                list.add(c);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY ID
    public Clinique readById(int id) throws SQLException {
        String sql = "SELECT * FROM cliniques WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String name  = rs.getString("name");
                String addr  = rs.getString("address");
                ArrayList<Room> rooms = roomRepo.readByCliniqueId(id);
                return new Clinique(id, name, addr, rooms);
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // UPDATE
    public void update(Clinique c) throws SQLException {
        String sql = "UPDATE cliniques SET name=?, address=? WHERE id=?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getAddress());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cliniques WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }
}
