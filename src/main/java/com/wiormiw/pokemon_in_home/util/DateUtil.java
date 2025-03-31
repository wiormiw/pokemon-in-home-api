package com.wiormiw.pokemon_in_home.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    // Default LocalDateTime format
    public static DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss");
    }

    // ISO Formatter
    public static DateTimeFormatter isoFormatter() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(DEFAULT_ZONE).toInstant());
    }
}
