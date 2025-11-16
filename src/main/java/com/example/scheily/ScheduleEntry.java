package com.example.scheily;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Defines the structure for a single scheduled class entry.
 * This is defined separately so both controllers can use it.
 */
public record ScheduleEntry(
        String name,
        String code,
        LocalTime startTime,
        LocalTime endTime,
        DayOfWeek day,
        String colorHex) {
}