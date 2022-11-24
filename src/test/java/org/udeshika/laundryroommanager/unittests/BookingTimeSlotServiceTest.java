package org.udeshika.laundryroommanager.unittests;

import org.junit.Assert;
import org.junit.Test;
import org.udeshika.laundryroommanager.database.repositories.TimeSlotRepository;
import org.udeshika.laundryroommanager.enums.LaundryEndTime;
import org.udeshika.laundryroommanager.enums.LaundryRoomId;
import org.udeshika.laundryroommanager.enums.LaundryStartTime;
import org.udeshika.laundryroommanager.models.BookingTimeSlot;
import org.udeshika.laundryroommanager.services.BookingTimeSlotService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * unit tests for BookingTimeSlotService
 */
public class BookingTimeSlotServiceTest {

    @Test
    public void bookTimeSlot_valid_booking_should_return_true() {
        TimeSlotRepository timeSlotRepositoryMock = mock(TimeSlotRepository.class);
        BookingTimeSlotService bookingTimeSlotService = new BookingTimeSlotService(timeSlotRepositoryMock);
        when(timeSlotRepositoryMock.insert(any())).thenReturn(true);

        BookingTimeSlot testTimeSlot = new BookingTimeSlot(LocalDate.of(2022,11,24),
                LaundryStartTime.TEN,
                LaundryEndTime.FOURTEEN,
                "U01", LaundryRoomId.L1.getValue());

        Assert.assertTrue(bookingTimeSlotService.bookTimeSlot(testTimeSlot));
    }

    @Test
    public void bookTimeSlot_invalid_timeslot_start_time_should_return_false() {
        TimeSlotRepository timeSlotRepositoryMock = mock(TimeSlotRepository.class);
        BookingTimeSlotService bookingTimeSlotService = new BookingTimeSlotService(timeSlotRepositoryMock);

        BookingTimeSlot testTimeSlot = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.FOURTEEN,
                LaundryEndTime.TEN,
                "U01", LaundryRoomId.L1.getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testTimeSlot));
        verifyNoInteractions(timeSlotRepositoryMock);
    }

    @Test
    public void bookTimeSlot_invalid_timeslot_should_return_false() {
        TimeSlotRepository timeSlotRepositoryMock = mock(TimeSlotRepository.class);
        BookingTimeSlotService bookingTimeSlotService = new BookingTimeSlotService(timeSlotRepositoryMock);

        BookingTimeSlot testTimeSlot = new BookingTimeSlot(LocalDate.now(),
                LaundryStartTime.TEN,
                LaundryEndTime.TWENTYTWO,
                "U01", LaundryRoomId.L1.getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testTimeSlot));
        verifyNoInteractions(timeSlotRepositoryMock);
    }

    @Test
    public void bookTimeSlot_past_booking_date_should_return_false() {
        TimeSlotRepository timeSlotRepositoryMock = mock(TimeSlotRepository.class);
        BookingTimeSlotService bookingTimeSlotService = new BookingTimeSlotService(timeSlotRepositoryMock);

        BookingTimeSlot testTimeSlot = new BookingTimeSlot(LocalDate.now().minusDays(1),
                LaundryStartTime.TEN,
                LaundryEndTime.FOURTEEN,
                "U01", LaundryRoomId.L1.getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testTimeSlot));
        verifyNoInteractions(timeSlotRepositoryMock);
    }

    @Test
    public void bookTimeSlot_booking_date_after_one_week_should_return_false() {
        TimeSlotRepository timeSlotRepositoryMock = mock(TimeSlotRepository.class);
        BookingTimeSlotService bookingTimeSlotService = new BookingTimeSlotService(timeSlotRepositoryMock);

        BookingTimeSlot testTimeSlot = new BookingTimeSlot(LocalDate.now().plusDays(8),
                LaundryStartTime.TEN,
                LaundryEndTime.FOURTEEN,
                "U01", LaundryRoomId.L1.getValue());

        Assert.assertFalse(bookingTimeSlotService.bookTimeSlot(testTimeSlot));
        verifyNoInteractions(timeSlotRepositoryMock);
    }
}
