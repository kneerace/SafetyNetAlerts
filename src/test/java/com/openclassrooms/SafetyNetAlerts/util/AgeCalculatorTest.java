package com.openclassrooms.SafetyNetAlerts.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgeCalculatorTest {

    private AgeCalculator ageCalculator = new AgeCalculator();
    @Test
    void calculateAge_invalidDate() {
        assertThrows(IllegalArgumentException.class, () -> ageCalculator.calculateAge(null, null));
        assertThrows(IllegalArgumentException.class, () -> ageCalculator.calculateAge("", null));
        assertThrows(IllegalArgumentException.class, () -> ageCalculator.calculateAge("1999-01-01", null));
        assertThrows(IllegalArgumentException.class, () -> ageCalculator.calculateAge("1999-01-01", ""));
        assertThrows(IllegalArgumentException.class, () -> ageCalculator.calculateAge(null, "yyyy-MM-dd"));
        assertThrows(IllegalArgumentException.class, () -> ageCalculator.calculateAge("2000-01-01", "yyyy-MM"));
    } // end of calculateAge_invalidDate

    @Test
    void calculateAge_validDate() {
        // today date
        String todayDate = java.time.LocalDate.now().toString();
        // 1 year ago
        String oneYearAgo = java.time.LocalDate.now().minusYears(1).toString();

        assertEquals(1, ageCalculator.calculateAge(oneYearAgo, "yyyy-MM-dd"));
        assertEquals(0, ageCalculator.calculateAge(todayDate, "yyyy-MM-dd"));

    } // end of calculateAge_validDate

    @Test
    void calculateAge_futureDate() {
        // get future date
        String futureDate = java.time.LocalDate.now().plusDays(1).toString();

        assertThrows(IllegalArgumentException.class, () -> ageCalculator.calculateAge(futureDate, "yyyy-MM-dd"));
    } // end of calculateAge_futureDate



} // end of AgeCalculatorTest