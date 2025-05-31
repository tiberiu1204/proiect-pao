package src.Services;

import src.Constants.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface AuditService {
    static final DateTimeFormatter TS_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String serviceName) throws IOException {
        String timestamp = LocalDateTime.now().format(TS_FMT);
        String line = serviceName + "," + timestamp + System.lineSeparator();

        Files.writeString(
                Constants.AUDIT_CSV,
                line,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND,
                StandardOpenOption.WRITE
        );
    }
}
