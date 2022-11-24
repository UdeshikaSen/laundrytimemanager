package org.udeshika.laundryroommanager;

import org.udeshika.laundryroommanager.database.repositories.implementation.LaundryRoomImpl;
import org.udeshika.laundryroommanager.database.repositories.implementation.TimeSlotRepositoryImpl;
import org.udeshika.laundryroommanager.database.repositories.implementation.UserRepositoryImpl;
import org.udeshika.laundryroommanager.enums.LaundryEndTime;
import org.udeshika.laundryroommanager.enums.LaundryRoomId;
import org.udeshika.laundryroommanager.enums.LaundryStartTime;
import org.udeshika.laundryroommanager.models.BookingTimeSlot;
import org.udeshika.laundryroommanager.models.LaundryRoom;
import org.udeshika.laundryroommanager.models.User;
import org.udeshika.laundryroommanager.services.BookingTimeSlotService;
import org.udeshika.laundryroommanager.services.LaundryRoomService;
import org.udeshika.laundryroommanager.services.UserService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaundryRoomManager {
    public static void main(String[] args) {
        UserService userService = new UserService(new UserRepositoryImpl());
        LaundryRoomService laundryRoomService = new LaundryRoomService(new LaundryRoomImpl());
        BookingTimeSlotService bookingTimeSlotService = new BookingTimeSlotService(new TimeSlotRepositoryImpl());

        // clean up db
        if (bookingTimeSlotService.cancelAllBookings()) {
            userService.removeAllUsers();
            laundryRoomService.removeAllRooms();
        }

        // test user
        User testsUser1 = new User("U01", "Udeshika");
        User testsUser2 = new User("U02", "Ellina");
        userService.registerUser(testsUser1);
        userService.registerUser(testsUser2);

        // test laundry rooms
        LaundryRoom testLaundryRoom1 = new LaundryRoom(LaundryRoomId.L1, LaundryStartTime.SEVEN.getValue(),
                LaundryEndTime.TWENTYTWO.getValue());
        LaundryRoom testLaundryRoom2 = new LaundryRoom(LaundryRoomId.L2, LaundryStartTime.SEVEN.getValue(),
                LaundryEndTime.TWENTYTWO.getValue());
        laundryRoomService.registerLaundryRoom(testLaundryRoom1);
        laundryRoomService.registerLaundryRoom(testLaundryRoom2);


        // Book (an available) laundry time.
        BookingTimeSlot testLaundryBooking1 = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());

        if (bookingTimeSlotService.bookTimeSlot(testLaundryBooking1)) {
            System.out.println(String.format("Laundry time added for user %s on %s at %s-%s", testsUser1.getName()
                    , testLaundryBooking1.getBookingDate(), testLaundryBooking1.getStartTime().getValue()
                    , testLaundryBooking1.getEndTime().getValue()));
            System.out.println();
        } else {
            System.out.println("Laundry booking failed");
        }

        // List booked times.
        BookingTimeSlot testLaundryBooking2 = new BookingTimeSlot(LocalDate.now().plusDays(1),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser2.getUserId(), testLaundryRoom2.getRoomId().getValue());

        bookingTimeSlotService.bookTimeSlot(testLaundryBooking2);

        System.out.println("List of all booked times");
        for (BookingTimeSlot bookedTime : bookingTimeSlotService.getAllBookedTimes()) {
            System.out.println(String.format("Date:%s  TimeSlot:%s-%s  Room:%s  User:%s "
                    , bookedTime.getBookingDate(), bookedTime.getStartTime().getValue(), bookedTime.getEndTime().getValue()
                    , bookedTime.getLaundryRoomId(), bookedTime.getUserId()));
        }
        System.out.println();

        // Cancelling the booking.
        System.out.println("Cancelling all bookings");

        // get the details of the booking to be deleted
        Optional<BookingTimeSlot> testDeleteTimeSLot1 = bookingTimeSlotService.
                getBookedTimeSlotsByUserInADay(testLaundryBooking1.getBookingDate(), testsUser1.getUserId());
        bookingTimeSlotService.cancelBooking(testsUser1.getUserId(), testDeleteTimeSLot1.get().getTimeSlotId());

        Optional<BookingTimeSlot> testDeleteTimeSLot2 = bookingTimeSlotService.
                getBookedTimeSlotsByUserInADay(testLaundryBooking2.getBookingDate(), testsUser2.getUserId());
        bookingTimeSlotService.cancelBooking(testsUser2.getUserId(), testDeleteTimeSLot2.get().getTimeSlotId());
    }
}
