package com.assignment.edu.fomatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    private DateTimeFormatter formatter;
    private String datePattern;

    public LocalDateFormatter(String datePattern) {
        this.datePattern = datePattern;
        formatter = DateTimeFormatter.ofPattern(datePattern);
    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        try {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern(datePattern));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use this pattern \""
                    + datePattern + "\"");
        }
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return date.format(formatter);
    }
}
