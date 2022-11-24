package org.udeshika.laundryroommanager.database.repositories.implementation;

import org.udeshika.laundryroommanager.database.DbConnection;
import org.udeshika.laundryroommanager.database.repositories.LaundryRoomRepository;
import org.udeshika.laundryroommanager.models.LaundryRoom;

import java.sql.*;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class LaundryRoomImpl implements LaundryRoomRepository {
    Connection dbConnection;
    static Logger logger = Logger.getLogger(LaundryRoomImpl.class.getName());

    public LaundryRoomImpl() {
        dbConnection = DbConnection.getConnection();
    }

    @Override
    public boolean insert(LaundryRoom room) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement
                    ("INSERT INTO laundryRoom(roomId, openTime, closeTime)" +
                            " VALUES(? ,?, ?)");
            stmt.setString(1, room.getRoomId().getValue());
            stmt.setTime(2, Time.valueOf(room.getOpeningTime()));
            stmt.setTime(3, Time.valueOf(room.getClosingTime()));
            return stmt.execute();
        } catch (SQLException e) {
            logger.log(INFO, String.format("DB error in inserting laundry room: %s  %s ", room.getRoomId(), e));
            return false;
        }
    }

    @Override
    public void deleteAll() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate("DELETE FROM laundryRoom");
        } catch (SQLException e) {
            logger.log(INFO, "DB error in deleting the users: " + e);
        }
    }
}
