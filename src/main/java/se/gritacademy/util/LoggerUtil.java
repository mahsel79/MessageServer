package se.gritacademy.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Stream;

public class LoggerUtil {
    private static final String LOG_DIR = "src/main/resources/logs";
    private static final String LOG_FILE = LOG_DIR + "/application.log";
    private static final long MAX_LOG_SIZE = 1024 * 1024; // 1MB
    private static final int MAX_LOG_FILES = 5;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create logs directory: " + e.getMessage());
        }
    }

    public enum LogLevel {
        INFO, WARN, ERROR
    }

    public static void log(String message) {
        log(LogLevel.INFO, message);
    }

    public static void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] %s - %s", level, timestamp, message);

        System.out.println(logEntry);

        try {
            rotateLogsIfNeeded();
            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write(logEntry + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }

    private static void rotateLogsIfNeeded() throws IOException {
        Path logPath = Paths.get(LOG_FILE);
        if (Files.exists(logPath) && Files.size(logPath) > MAX_LOG_SIZE) {
            // Rotate logs
            for (int i = MAX_LOG_FILES - 1; i > 0; i--) {
                Path oldFile = Paths.get(LOG_DIR, "application." + i + ".log");
                Path newFile = Paths.get(LOG_DIR, "application." + (i + 1) + ".log");
                if (Files.exists(oldFile)) {
                    Files.move(oldFile, newFile);
                }
            }
            Files.move(logPath, Paths.get(LOG_DIR, "application.1.log"));
        }

        // Clean up old logs
        try (Stream<Path> files = Files.list(Paths.get(LOG_DIR))) {
            files.filter(Files::isRegularFile)
                 .filter(p -> p.getFileName().toString().matches("application\\.\\d+\\.log"))
                 .sorted(Comparator.reverseOrder())
                 .skip(MAX_LOG_FILES)
                 .forEach(p -> {
                     try {
                         Files.delete(p);
                     } catch (IOException e) {
                         System.err.println("Failed to delete old log file: " + p);
                     }
                 });
        }
    }
}
