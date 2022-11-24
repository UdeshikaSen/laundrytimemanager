package org.udeshika.laundryroommanager.models;

import org.udeshika.laundryroommanager.enums.LaundryRoomId;

import java.time.LocalTime;

public class LaundryRoom {
    private LaundryRoomId roomId;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public LaundryRoom(LaundryRoomId roomId, LocalTime openingTime, LocalTime closingTime) {
        this.roomId = roomId;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public LaundryRoomId getRoomId() {
        return roomId;
    }

    public void setRoomId(LaundryRoomId roomId) {
        this.roomId = roomId;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
