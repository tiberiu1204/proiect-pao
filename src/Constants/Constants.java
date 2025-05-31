package src.Constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public static final String JDBC_URL = "jdbc:mariadb://localhost:3306/proiect_pao";
    public static final String DATABASE_CREDENTIALS = "./db_credentials.csv";
    public static final Path AUDIT_CSV = Paths.get("./audit.csv");
}
