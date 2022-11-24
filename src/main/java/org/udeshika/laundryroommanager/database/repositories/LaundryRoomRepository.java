package org.udeshika.laundryroommanager.database.repositories;

import org.udeshika.laundryroommanager.models.LaundryRoom;

public interface LaundryRoomRepository {
    boolean insert(LaundryRoom room);
    void deleteAll();
}
