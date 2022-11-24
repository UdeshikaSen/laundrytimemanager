package org.udeshika.laundryroommanager.database.repositories;

import org.udeshika.laundryroommanager.models.BookingTimeSlot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository {
    boolean insert(BookingTimeSlot bookingTimeSlot);

    List<BookingTimeSlot> getAll();

    List<BookingTimeSlot> getBookedTimeSlotsByDate(LocalDate onDate);

    Optional<BookingTimeSlot> getBookedTimeSlotByUserInADay(LocalDate onDate, String userId);

    boolean delete(String userId, int timeSlotId);

    boolean deleteAll();
}