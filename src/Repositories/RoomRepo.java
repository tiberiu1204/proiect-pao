package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Room;
import src.Utils.RoomType;

import java.sql.*;
import java.util.ArrayList;

public class RoomRepo {
    private final DatabaseConfiguration dbConfig;

    public RoomRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig = dbConfig;
    }

    // CREATE
    public void create(Room r, int cliniqueId) throws SQLException {
        String sql = "INSERT INTO rooms "
                + "(clinique_id, room_type, floor, room_number) VALUES (?,?,?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cliniqueId);
            ps.setString(2, r.getType().name());
            ps.setInt(3, r.getFloor());
            ps.setInt(4, r.getRoomNumber());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setId(keys.getInt(1));
                }
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<Room> readAll() throws SQLException {
        String sql = "SELECT * FROM rooms";
        ArrayList<Room> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Room r = new Room(
                        rs.getInt("id"),
                        RoomType.valueOf(rs.getString("room_type")),
                        rs.getInt("floor"),
                        rs.getInt("room_number")
                );
                list.add(r);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY CLINIQUE_ID
    public ArrayList<Room> readByCliniqueId(int cliniqueId) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE clinique_id = ?";
        ArrayList<Room> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliniqueId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Room r = new Room(
                            rs.getInt("id"),
                            RoomType.valueOf(rs.getString("room_type")),
                            rs.getInt("floor"),
                            rs.getInt("room_number")
                    );
                    list.add(r);
                }
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // UPDATE
    public void update(Room r) throws SQLException {
        String sql = "UPDATE rooms SET room_type=?, floor=?, room_number=? WHERE id=?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getType().name());
            ps.setInt(2, r.getFloor());
            ps.setInt(3, r.getRoomNumber());
            ps.setInt(4, r.getId());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }
}
