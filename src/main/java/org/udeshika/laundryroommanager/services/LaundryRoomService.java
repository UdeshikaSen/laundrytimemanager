package org.udeshika.laundryroommanager.services;

import org.udeshika.laundryroommanager.database.repositories.LaundryRoomRepository;
import org.udeshika.laundryroommanager.database.repositories.implementation.LaundryRoomImpl;
import org.udeshika.laundryroommanager.models.LaundryRoom;

import java.util.logging.Logger;

/**
 * A service class responsible for managing laundry rooms.
 */
public class LaundryRoomService {
    private final Logger logger = Logger.getLogger(LaundryRoomService.class.getName());
    private final LaundryRoomRepository laundryRoomRepository;

    public LaundryRoomService(LaundryRoomRepository laundryRoomRepository) {
        this.laundryRoomRepository = laundryRoomRepository;
    }

    public boolean registerLaundryRoom(LaundryRoom laundryRoom) {
        return laundryRoomRepository.insert(laundryRoom);
    }

    /**
     * Renove all rooms for test purposes
     *
     * @return indicates whether successfully deleted
     */
    public void removeAllRooms() {
        laundryRoomRepository.deleteAll();
    }
}