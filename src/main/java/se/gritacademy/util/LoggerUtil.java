package se.gritacademy.util;

import java.time.LocalDateTime;

public class LoggerUtil {
    public static void log(String message) {
        System.out.println(LocalDateTime.now() + " - " + message);
    }
}