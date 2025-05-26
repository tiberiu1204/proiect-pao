package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Person;

import java.sql.*;
import java.util.ArrayList;

public class PersonRepo {
    private final DatabaseConfiguration dbConfig;

    public PersonRepo(DatabaseConfiguration dbConfig) {
        this.dbConfig = dbConfig;
    }

    // CREATE → fetches auto‐gen id
    public void create(Person p) throws SQLException {
        String sql = "INSERT INTO persons "
                + "(first_name, last_name, age, birth, phone_number, email, address) "
                + "VALUES (?,?,?,?,?,?,?)";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setInt(3, p.getAge());
            ps.setDate(4, new java.sql.Date(p.getBirth().getTime()));
            ps.setString(5, p.getPhoneNumber());
            ps.setString(6, (String) p.getEmail());
            ps.setString(7, p.getAddress());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // READ ALL
    public ArrayList<Person> readAll() throws SQLException {
        String sql = "SELECT * FROM persons";
        ArrayList<Person> list = new ArrayList<>();
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Person p = new Person(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getDate("birth"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                list.add(p);
            }
        } finally {
            dbConfig.close(conn);
        }
        return list;
    }

    // READ BY ID
    public Person readById(int id) throws SQLException {
        String sql = "SELECT * FROM persons WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Person(
                        id,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getDate("birth"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }
        } finally {
            dbConfig.close(conn);
        }
    }

    // UPDATE
    public void update(Person p) throws SQLException {
        String sql = "UPDATE persons SET "
                + "first_name=?, last_name=?, age=?, birth=?, phone_number=?, email=?, address=? "
                + "WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setInt(3, p.getAge());
            ps.setDate(4, new java.sql.Date(p.getBirth().getTime()));
            ps.setString(5, p.getPhoneNumber());
            ps.setString(6, (String) p.getEmail());
            ps.setString(7, p.getAddress());
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM persons WHERE id = ?";
        Connection conn = dbConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            dbConfig.close(conn);
        }
    }
}
