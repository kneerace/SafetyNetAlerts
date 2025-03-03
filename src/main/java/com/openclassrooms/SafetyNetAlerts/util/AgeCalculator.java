package com.openclassrooms.SafetyNetAlerts.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.time.format.DateTimeParseException;

public class AgeCalculator {
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static int calculateAge(String birthDate, String dateFormat) {
        if(birthDate == null || birthDate.isEmpty()
            || dateFormat == null || dateFormat.isEmpty()) {
            throw new IllegalArgumentException("birthDate and dateFormat cannot be null or empty");
        } // end if

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        try {
            LocalDate parsedBirthDate = LocalDate.parse(birthDate, formatter);

            LocalDate currentDate = LocalDate.now();

            if (parsedBirthDate.isAfter(currentDate)) {
                throw new IllegalArgumentException("birthDate cannot be in the future");
            }

            return Period.between(parsedBirthDate, currentDate).getYears();
        }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: " + dateFormat , e);
        }
    } //  end calculateAge
} // end class
