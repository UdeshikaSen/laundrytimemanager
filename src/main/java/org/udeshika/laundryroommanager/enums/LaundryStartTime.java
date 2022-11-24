package org.udeshika.laundryroommanager.enums;

import java.time.LocalTime;

public enum LaundryStartTime {

    SEVEN(LocalTime.of(7,00)),
    TEN(LocalTime.of(10,00)),
    FOURTEEN(LocalTime.of(14,00)),
    EIGHTEEN(LocalTime.of(18,00));

    private final LocalTime startTime;
    LaundryStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getValue() {
        return startTime;
    }

    public static LaundryStartTime fromTime(LocalTime time) {
        for(LaundryStartTime laundryStartTime: LaundryStartTime.values()) {
            if(laundryStartTime.startTime.equals(time)) {
                return laundryStartTime;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + time);
    }
}
