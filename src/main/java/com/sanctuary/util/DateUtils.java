package com.sanctuary.util;

import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    private static final DateTimeFormatter STORAGE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String today() {
        return LocalDate.now().format(STORAGE_FMT);
    }

    public static String now() {
        return LocalDateTime.now().format(TIMESTAMP_FMT);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, STORAGE_FMT);
    }

    public static String formatStorage(LocalDate date) {
        return date.format(STORAGE_FMT);
    }

    public static String formatDisplay(LocalDate date) {
        return date.format(DISPLAY_FMT);
    }

    public static long daysBetween(String start, String end) {
        return ChronoUnit.DAYS.between(parseDate(start), parseDate(end));
    }

    public static String addDays(String dateStr, long days) {
        return formatStorage(parseDate(dateStr).plusDays(days));
    }

    public static String addMonths(String dateStr, long months) {
        return formatStorage(parseDate(dateStr).plusMonths(months));
    }

    public static boolean isAfter(String dateStr) {
        return parseDate(dateStr).isAfter(LocalDate.now());
    }

    public static boolean isBefore(String dateStr) {
        return parseDate(dateStr).isBefore(LocalDate.now());
    }

    public static boolean isOverdue(String dueDate) {
        return parseDate(dueDate).isBefore(LocalDate.now());
    }
}
