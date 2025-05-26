package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Disease;
import src.Utils.DiseaseType;

import java.sql.*;
import java.util.ArrayList;

public class DiseaseRepo {
    private final DatabaseConfiguration dbConfig;

    public DiseaseRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig = dbConfig;
    }

    // CREATE
    public void create(Disease d) throws SQLException {
        String sql = "INSERT INTO diseases (name, type) VALUES (?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getType().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    d.setId(keys.getInt(1));
                }
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<Disease> readAll() throws SQLException {
        String sql = "SELECT * FROM diseases";
        ArrayList<Disease> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Disease d = new Disease(
                        rs.getInt("id"),
                        rs.getString("name"),
                        DiseaseType.valueOf(rs.getString("type"))
                );
                list.add(d);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY ID
    public Disease readById(int id) throws SQLException {
        String sql = "SELECT * FROM diseases WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Disease(
                        id,
                        rs.getString("name"),
                        DiseaseType.valueOf(rs.getString("type"))
                );
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // UPDATE
    public void update(Disease d) throws SQLException {
        String sql = "UPDATE diseases SET name=?, type=? WHERE id=?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getType().name());
            ps.setInt(3, d.getId());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM diseases WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }
}
