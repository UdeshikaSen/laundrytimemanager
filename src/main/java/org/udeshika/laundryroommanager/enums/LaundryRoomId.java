package org.udeshika.laundryroommanager.enums;

public enum LaundryRoomId {
    L1("L01"),
    L2("L02");

    private final String laundryRoom;

    LaundryRoomId(String room) {
        this.laundryRoom = room;
    }

    public String getValue(){
        return laundryRoom;
    }
}
