package src.Config;

import src.Utils.ReadFromFile;
import src.Constants.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {

    private final String username;
    private final String password;

    public DatabaseConfiguration(String credentialsCsv) {
        String[] creds = ReadFromFile.readCredentials(credentialsCsv);
        if (creds.length != 2)
            throw new IllegalArgumentException("CSV must have two values: username,password");
        this.username = creds[0];
        this.password = creds[1];
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Constants.JDBC_URL, username, password);
    }

    public void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}