package org.udeshika.laundryroommanager.services.helpers;

import org.udeshika.laundryroommanager.database.repositories.TimeSlotRepository;
import org.udeshika.laundryroommanager.enums.LaundryStartTime;
import org.udeshika.laundryroommanager.models.BookingTimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Logger;

import static java.time.temporal.ChronoUnit.*;
import static java.util.logging.Level.INFO;

public class BookingTimeSlotServiceHelper {
    private final TimeSlotRepository timeSlotRepository;
    private final Logger logger = Logger.getLogger(BookingTimeSlotServiceHelper.class.getName());

    public BookingTimeSlotServiceHelper(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    /**
     * Pre validates the booking information
     * 1) Time slot should be during the opening hours (7-22) of the laundry rooms
     * 2) Time slot should be one of pre-defined time slots (7-10, 10-14, 14-18, 18-22)
     * 3) Booking date should be valid
     * 4) A user can not book 1 week ahead of time
     *
     * @param bookingTimeSlot contains booking information
     * @return indicates whether the booking information is valid
     */
    public boolean validateBookingInformation(BookingTimeSlot bookingTimeSlot) {
        if (!isValidBookingDate(bookingTimeSlot.getBookingDate())) {
            logger.log(INFO, "Booking date is invalid.");
            return false;
        }
        if (!isValidBookingSlot(bookingTimeSlot.getStartTime().getValue(), bookingTimeSlot.getEndTime().getValue())) {
            logger.log(INFO, "Booking time slot is invalid");
            return false;
        }
        // if the user already has booking in this particular day, another booking is not valid.
        if (timeSlotRepository.getBookedTimeSlotByUserInADay(bookingTimeSlot.getBookingDate(),
                bookingTimeSlot.getUserId()).isPresent()) {
            logger.log(INFO, "User can't book multiple slots on the same day.");
            return false;
        }
        return true;
    }

    /**
     * Validates the booking time slot
     * 1) Booking start time should be lesser than end time.
     * 2) Time slot should be one of the pre-defined time slots.
     *
     * @param bookingStartTime
     * @param bookingEndTime
     * @return indicates whether the time slot is valid
     */
    private boolean isValidBookingSlot(LocalTime bookingStartTime, LocalTime bookingEndTime) {
        if (bookingStartTime.isAfter(bookingEndTime)) {
            return false;
        }

        if (bookingStartTime.equals(LaundryStartTime.SEVEN.getValue())) {
            // time slot starting at 7am has aa duration of 3 hours
            return ((bookingStartTime).until(bookingEndTime, HOURS) == 3);
        } else {
            // all other time slots have a duration of 4 hours
            return ((bookingStartTime).until(bookingEndTime, HOURS) == 4);
        }
    }

    /**
     * Validates the booking date. It should be a future date but lesser than 1 week time.
     *
     * @param bookingDate
     * @return indicates whether the booking date is valid
     */
    private boolean isValidBookingDate(LocalDate bookingDate) {
        return bookingDate.isAfter(LocalDate.now().minus(1, DAYS))
                && bookingDate.until(LocalDate.now(), WEEKS) == 0;
    }
}
