package nl.devcraft.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record Timeslot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {}
