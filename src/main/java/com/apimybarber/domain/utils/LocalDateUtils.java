package com.apimybarber.domain.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateUtils {

    public static String getDataFormatada(LocalDate localDate) {
        try {
            if (localDate == null) {
                return "";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return localDate.format(formatter);
        } catch (Exception e) {
            throw new RuntimeException("Erro getDataFormatada:" + e);
        }
    }

    public static String getDataFormatada(LocalDate localDate, String mask) {
        try {
            if (localDate == null) {
                return "";
            }
            return new SimpleDateFormat(mask).format(localDate);
        } catch (Exception e) {
            throw new RuntimeException("Erro getDataFormatada:" + e);
        }
    }

    public static LocalDateTime getLocalDateTimeIso(String localDateTimeIso8601) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            return LocalDateTime.parse(localDateTimeIso8601, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Erro getLocalDateTimeIso:" + e);
        }
    }

    public static LocalDate getLocalDateIso(String localDateTimeIso8601) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeIso8601, formatter);
            return localDateTime.toLocalDate();
        } catch (Exception e) {
            throw new RuntimeException("Erro getLocalDateIso:" + e);
        }
    }

    public static String getLocalDateStringIso(LocalDateTime localDateTimeIso8601) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return localDateTimeIso8601.format(formatter);
        } catch (Exception e) {
            throw new RuntimeException("Erro getLocalDateIso:" + e);
        }
    }

    public static LocalDate getLocalDateTimestamp(Timestamp timestamp) {
        try {
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            return localDateTime.toLocalDate();
        } catch (Exception e) {
            throw new RuntimeException("Erro getLocalDateTimestamp:" + e);
        }
    }

}
