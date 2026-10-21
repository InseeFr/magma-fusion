package fr.insee.rmes.magmafusion.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

@Slf4j
public class DatesUtils {
    private DatesUtils() {
        /* This utility class should not be instantiated */
    }
    public static LocalDate tryParseDateToLocalDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        // Try first to parse as Instant
        try {
            return Instant.parse(dateString)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (DateTimeParseException e1) {
            // If failed, try to parse as LocalDateTime
            try {
                return LocalDateTime.parse(dateString).toLocalDate();
            } catch (DateTimeParseException e2) {
                log.error("IMPOSSIBLE TO PARSE THE DATE '{}' : {}", dateString, e2.getMessage());
                return null;
            }
        }
    }
}
