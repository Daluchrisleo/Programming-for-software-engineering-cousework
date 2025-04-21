package com.boostphysioclinic.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting {@link LocalDateTime} objects into
 * a readable string format.
 */
public class TimeFormatter {

    /** Formatter for displaying date and time in a friendly format like "Monday, 01 January 2025 - 2:00 PM" */
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - h:mm a");

    /**
     * Formats the given {@link LocalDateTime} into a string representation.
     *
     * @param time the LocalDateTime to format
     * @return a formatted string of the date and time
     */
    public static String formatTime(LocalDateTime time) {
        return time.format(formatter);
    }
}
