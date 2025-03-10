package com.mupl.payment_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyMMdd = "yyMMdd";

    public static String convertDateTimeToString(LocalDateTime dateTime, String pattern) {
        return createDateTimeFormatter(pattern).format(dateTime);
    }

    public static LocalDateTime convertStringToDateTime(String dateTime, String pattern) {
        return LocalDateTime.parse(dateTime, createDateTimeFormatter(pattern));
    }

    private static DateTimeFormatter createDateTimeFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }
}
