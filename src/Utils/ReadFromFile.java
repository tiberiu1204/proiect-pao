package src.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFromFile {
    public static String[] readCredentials(String csvPath) {
        try {
            String line = Files.readAllLines(Paths.get(csvPath)).getFirst().trim();
            return line.split(",");
        } catch (IOException e) {
            throw new RuntimeException("Unable to read DB credentials from " + csvPath, e);
        }
    }
}
