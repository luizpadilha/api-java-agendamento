package com.apimybarber.domain.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateUtils {

    public static String getDataFormatada(LocalDate data) {
        try {
            if (data == null) {
                return "";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return data.format(formatter);
        } catch (Exception e) {
            throw new RuntimeException("Erro getDataFormatada:" + e);
        }
    }

    public static String getDataFormatada(LocalDate data, String mask) {
        try {
            if (data == null) {
                return "";
            }
            return new SimpleDateFormat(mask).format(data);
        } catch (Exception e) {
            throw new RuntimeException("Erro getDataFormatada:" + e);
        }
    }

    public static LocalDateTime getLocalDateTimeIso(String dataIso8601) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            return LocalDateTime.parse(dataIso8601, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Erro getLocalDateTimeIso:" + e);
        }
    }

    public static LocalDate getLocalDateIso(String dataIso8601) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(dataIso8601, formatter);
            return localDateTime.toLocalDate();
        } catch (Exception e) {
            throw new RuntimeException("Erro getLocalDateIso:" + e);
        }
    }

}
