package org.udeshika.laundryroommanager.services;

import org.udeshika.laundryroommanager.database.repositories.TimeSlotRepository;
import org.udeshika.laundryroommanager.models.BookingTimeSlot;
import org.udeshika.laundryroommanager.services.helpers.BookingTimeSlotServiceHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * A service class responsible for managing booking time slots.
 */
public class BookingTimeSlotService {
    private final Logger logger = Logger.getLogger(BookingTimeSlotService.class.getName());
    private final TimeSlotRepository timeSlotRepository;
    private final BookingTimeSlotServiceHelper serviceHelper;

    public BookingTimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
        serviceHelper = new BookingTimeSlotServiceHelper(timeSlotRepository);
    }

    /**
     * Books an available time slot of the provided laundry room
     *
     * @param bookingTimeSlot contains booking information
     * @return indicates whether the booking is successful
     */
    public boolean bookTimeSlot(BookingTimeSlot bookingTimeSlot) {
        if (serviceHelper.validateBookingInformation(bookingTimeSlot)) {
            return timeSlotRepository.insert(bookingTimeSlot);
        } else {
            logger.log(WARNING, String.format("Failed to book the laundry room %s for the user %s.",
                    bookingTimeSlot.getLaundryRoomId(), bookingTimeSlot.getUserId()));
            return false;
        }
    }

    /**
     * Returns all the booked time slots of all laundry rooms.
     *
     * @return list of all the booked time slots
     */
    public List<BookingTimeSlot> getAllBookedTimes() {
        return timeSlotRepository.getAll();
    }

    /**
     * Returns booked time slots of all laundry rooms in the provided date
     *
     * @param onDate
     * @return list of booked time slots in a day
     */
    public List<BookingTimeSlot> getBookedTimesByDate(LocalDate onDate) {
        return timeSlotRepository.getBookedTimeSlotsByDate(onDate);
    }

    /**
     * Returns all booked time slots of a user in the provided date
     *
     * @param onDate
     * @return list of booked time slots of a user in a day
     */
    public Optional<BookingTimeSlot> getBookedTimeSlotsByUserInADay(LocalDate onDate, String userId) {
        return timeSlotRepository.getBookedTimeSlotByUserInADay(onDate, userId);
    }

    /**
     * Cancels the booking by soft deleting
     *
     * @param userId
     * @param timeSlotId
     * @return indicates whether successfully marked the record as deleted
     */
    public boolean cancelBooking(String userId, int timeSlotId) {
        return timeSlotRepository.delete(userId, timeSlotId);
    }

    /**
     * Cancels all bookings for test purposes
     *
     * @return indicates whether successfully marked the record as deleted
     */
    public boolean cancelAllBookings() {
        return timeSlotRepository.deleteAll();
    }
}
