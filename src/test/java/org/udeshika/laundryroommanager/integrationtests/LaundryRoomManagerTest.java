package org.udeshika.laundryroommanager.integrationtests;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.*;
import org.testcontainers.containers.MySQLContainer;
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

public class LaundryRoomManagerTest {
    private static BookingTimeSlotService bookingTimeSlotService;
    private static UserService userService;
    private static LaundryRoomService laundryRoomService;
    private static User testsUser1;
    private static User testsUser2;
    private static LaundryRoom testLaundryRoom1;
    private static LaundryRoom testLaundryRoom2;
    private BookingTimeSlot testLaundryBooking;
    private static MySQLContainer mysql = new MySQLContainer<>("mysql:8.0.25")
            .withInitScript("db_script.sql")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("laundry_room_booking_db")
            .withExposedPorts(3306)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(3306), new ExposedPort(3306)))
            ));
    private static TimeSlotRepositoryImpl timeSlotRepository;

    @BeforeClass
    public static void setUp() {
        mysql.start();
        timeSlotRepository = new TimeSlotRepositoryImpl();
        bookingTimeSlotService = new BookingTimeSlotService(timeSlotRepository);

        // test user
        userService = new UserService(new UserRepositoryImpl());
        testsUser1 = new User("U01", "Udeshika");
        testsUser2 = new User("U02", "Ellina");

        userService.registerUser(testsUser1);
        userService.registerUser(testsUser2);

        // test laundry rooms
        laundryRoomService = new LaundryRoomService(new LaundryRoomImpl());
        testLaundryRoom1 = new LaundryRoom(LaundryRoomId.L1, LaundryStartTime.SEVEN.getValue(),
                LaundryEndTime.TWENTYTWO.getValue());
        testLaundryRoom2 = new LaundryRoom(LaundryRoomId.L2, LaundryStartTime.SEVEN.getValue(),
                LaundryEndTime.TWENTYTWO.getValue());

        laundryRoomService.registerLaundryRoom(testLaundryRoom1);
        laundryRoomService.registerLaundryRoom(testLaundryRoom2);
    }

    @AfterClass
    public static void cleanUp() {
        mysql.stop();
    }

    @Before
    public void beforeEachTest() {
        timeSlotRepository.deleteAll();
    }

    /**
     * test scenario - Book (an available) laundry time.
     */
    @Test
    public void book_valid_laundry_time_should_return_true() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.TEN,
                LaundryEndTime.FOURTEEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());

        Assert.assertTrue(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));
    }

    /**
     * test scenario - Two users book the same laundry time of a room.
     */
    @Test
    public void two_users_books_same_laundry_time_should_return_false() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());
        bookingTimeSlotService.bookTimeSlot(testLaundryBooking);

        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser2.getUserId(), testLaundryRoom1.getRoomId().getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));
    }

    /**
     * test scenario - User books another time slot of the same room in the same day
     */
    @Test
    public void same_user_books_another_timeslot_of_same_laundry_room_on_same_day_should_return_false() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());
        bookingTimeSlotService.bookTimeSlot(testLaundryBooking);

        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.FOURTEEN,
                LaundryEndTime.EIGHTEEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));
    }

    /**
     * test scenario - User books another time slot of the other room in the same day
     */
    @Test
    public void same_user_book_other_laundry_room_on_same_day_should_return_false() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());
        bookingTimeSlotService.bookTimeSlot(testLaundryBooking);

        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom2.getRoomId().getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));
    }

    /**
     * test scenario - Two users book the same time of each of the 2 rooms.
     */
    @Test
    public void book_two_rooms_on_same_laundry_time_should_return_true() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());
        bookingTimeSlotService.bookTimeSlot(testLaundryBooking);

        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser2.getUserId(), testLaundryRoom2.getRoomId().getValue());

        Assert.assertTrue(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));
    }


    /**
     * test scenario - Book a time duration greater than durations that is pre-defined.
     */
    @Test
    public void book_invalid_timeslot_should_return_false() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.EIGHTEEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));
    }

    /**
     * test scenario - Book a time ahead of one week.
     */
    @Test
    public void book_timeslot_1_week_ahead_should_return_false() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now().plusDays(9),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));
    }

    /**
     * test scenario - Cancel a booking.
     */
    @Test
    public void cancel_booked_slot_should_return_true() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());
        bookingTimeSlotService.bookTimeSlot(testLaundryBooking);

        Optional<BookingTimeSlot> bookedTimeSlotsByUserInADay = bookingTimeSlotService.
                getBookedTimeSlotsByUserInADay(LocalDate.now(), testsUser1.getUserId());

        Assert.assertTrue(bookedTimeSlotsByUserInADay.isPresent());
        Assert.assertTrue(bookingTimeSlotService.cancelBooking(testsUser1.getUserId(), bookedTimeSlotsByUserInADay.get().getTimeSlotId()));
    }

    /**
     * test scenario - List all booked times
     */
    @Test
    public void retrieve_all_booked_times_should_return_correct_count() {
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.SEVEN,
                LaundryEndTime.TEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());
        Assert.assertTrue(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));

        // this booking should fail
        testLaundryBooking = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.FOURTEEN,
                LaundryEndTime.EIGHTEEN,
                testsUser1.getUserId(), testLaundryRoom1.getRoomId().getValue());
        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));

        testLaundryBooking = new BookingTimeSlot(LocalDate.now().plusDays(1),
                LaundryStartTime.TEN,
                LaundryEndTime.FOURTEEN,
                testsUser2.getUserId(), testLaundryRoom1.getRoomId().getValue());
        Assert.assertTrue(bookingTimeSlotService.bookTimeSlot(testLaundryBooking));

        // expecting only 2 bookings to be successful after making 3 bookings.
        Assert.assertEquals(2, bookingTimeSlotService.getAllBookedTimes().size());
    }
}


