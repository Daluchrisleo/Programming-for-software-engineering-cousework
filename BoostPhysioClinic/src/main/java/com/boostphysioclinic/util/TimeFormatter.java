package com.boostphysioclinic.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - h:mm a");

    public static String formatTime(LocalDateTime time) {
        return time.format(formatter);
    }
}
