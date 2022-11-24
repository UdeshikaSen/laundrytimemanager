package org.udeshika.laundryroommanager.enums;

import java.time.LocalTime;

public enum LaundryEndTime {

    TEN(LocalTime.of(10,00)),
    FOURTEEN(LocalTime.of(14,00)),
    EIGHTEEN(LocalTime.of(18,00)),
    TWENTYTWO(LocalTime.of(22,00));

    private final LocalTime endTime;
    LaundryEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getValue() {
        return endTime;
    }

    public static LaundryEndTime fromTime(LocalTime time) {
        for(LaundryEndTime laundryEndTime: LaundryEndTime.values()) {
            if(laundryEndTime.endTime.equals(time)) {
                return laundryEndTime;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + time);
    }
}
