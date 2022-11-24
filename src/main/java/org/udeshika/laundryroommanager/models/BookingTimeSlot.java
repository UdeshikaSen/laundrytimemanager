package org.udeshika.laundryroommanager.models;

import org.udeshika.laundryroommanager.enums.LaundryEndTime;
import org.udeshika.laundryroommanager.enums.LaundryStartTime;

import java.time.LocalDate;

public class BookingTimeSlot {
    private int timeSlotId;
    private LocalDate bookingDate;
    private LaundryStartTime startTime;
    private LaundryEndTime endTime;
    private String userId;
    private String laundryRoomId;

    public BookingTimeSlot(LocalDate bookedDate, LaundryStartTime startTime, LaundryEndTime endTime, String userId, String laundryRoomId) {
        this.bookingDate = bookedDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.laundryRoomId = laundryRoomId;
    }

    public BookingTimeSlot(int timeSlotId, LocalDate bookingDate, LaundryStartTime startTime, LaundryEndTime endTime, String userId, String laundryRoomId) {
        this.timeSlotId = timeSlotId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.laundryRoomId = laundryRoomId;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LaundryStartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LaundryStartTime startTime) {
        this.startTime = startTime;
    }

    public LaundryEndTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LaundryEndTime endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLaundryRoomId() {
        return laundryRoomId;
    }

    public void setLaundryRoomId(String laundryRoomId) {
        this.laundryRoomId = laundryRoomId;
    }
}
