package org.udeshika.laundryroommanager.database.repositories.implementation;

import org.udeshika.laundryroommanager.database.DbConnection;
import org.udeshika.laundryroommanager.database.repositories.TimeSlotRepository;
import org.udeshika.laundryroommanager.enums.LaundryEndTime;
import org.udeshika.laundryroommanager.enums.LaundryStartTime;
import org.udeshika.laundryroommanager.models.BookingTimeSlot;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

public class TimeSlotRepositoryImpl implements TimeSlotRepository {

    Connection dbConnection;
    static Logger logger = Logger.getLogger(TimeSlotRepositoryImpl.class.getName());

    public TimeSlotRepositoryImpl() {
        dbConnection = DbConnection.getConnection();
    }

    /**
     * Save a new laundry room booking the bookingTimeSlot table
     *
     * @param bookingTimeSlot
     * @return indicates whether the time slot was saved successfully ot the DB
     */
    @Override
    public boolean insert(BookingTimeSlot bookingTimeSlot) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement
                    ("INSERT INTO bookingTimeSlot(bookingDate, startTime, endTime, bookedUserId, laundryRoomId)" +
                            " VALUES(? ,?, ?, ?, ?)");
            stmt.setDate(1, Date.valueOf(bookingTimeSlot.getBookingDate()));
            stmt.setTime(2, Time.valueOf(bookingTimeSlot.getStartTime().getValue()));
            stmt.setTime(3, Time.valueOf(bookingTimeSlot.getEndTime().getValue()));
            stmt.setString(4, bookingTimeSlot.getUserId());
            stmt.setString(5, bookingTimeSlot.getLaundryRoomId());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            logger.log(INFO, "DB error in inserting the time slots: " + e);
            return false;
        }
    }

    /**
     * Retrieves all laundry room bookings
     *
     * @return a list of booked time slots
     */
    @Override
    public List<BookingTimeSlot> getAll() {
        List<BookingTimeSlot> bookedTimeSlots = new ArrayList<>();
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bookingTimeSlot WHERE deletedAtUnixTime = -1");

            while (rs.next()) {
                bookedTimeSlots.add(new BookingTimeSlot(
                        rs.getInt("timeSlotId"),
                        rs.getDate("bookingDate").toLocalDate(),
                        LaundryStartTime.fromTime(rs.getTime("startTime").toLocalTime()),
                        LaundryEndTime.fromTime(rs.getTime("endTime").toLocalTime()),
                        rs.getString("bookedUserId"),
                        rs.getString("laundryRoomId")
                ));
            }
            return bookedTimeSlots;
        } catch (SQLException e) {
            logger.log(INFO, "DB error in getting time slots: " + e);
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves all the laundry room bookings in a given date
     *
     * @param onDate
     * @return a list of booked time slots in a day
     */
    @Override
    public List<BookingTimeSlot> getBookedTimeSlotsByDate(LocalDate onDate) {
        List<BookingTimeSlot> bookedTimeSlots = new ArrayList<>();

        try {
            PreparedStatement stmt = dbConnection.prepareStatement
                    ("SELECT * FROM bookingTimeSlot WHERE bookingDate = ? AND deletedAtUnixTime = -1");
            stmt.setDate(1, Date.valueOf(onDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookedTimeSlots.add(new BookingTimeSlot(
                                rs.getInt("timeSlotId"),
                                rs.getDate("bookingDate").toLocalDate(),
                                LaundryStartTime.fromTime(rs.getTime("startTime").toLocalTime()),
                                LaundryEndTime.fromTime(rs.getTime("endTime").toLocalTime()),
                                rs.getString("bookedUserId"),
                                rs.getString("laundryRoomId")
                        )
                );
            }
            return bookedTimeSlots;
        } catch (SQLException e) {
            logger.log(INFO, "DB error in getting time slots: " + e);
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves the laundry room booking of a user of a given day
     *
     * @param onDate
     * @param userId
     * @return user's booked time slot in a given day
     */
    @Override
    public Optional<BookingTimeSlot> getBookedTimeSlotByUserInADay(LocalDate onDate, String userId) {
        BookingTimeSlot bookedTimeSlot = null;
        try {
            PreparedStatement stmt = dbConnection.prepareStatement
                    ("SELECT * FROM bookingTimeSlot WHERE bookingDate = ? AND bookedUserId = ? AND" +
                            " deletedAtUnixTime = -1");
            stmt.setDate(1, Date.valueOf(onDate));
            stmt.setString(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookedTimeSlot = new BookingTimeSlot(
                        rs.getInt("timeSlotId"),
                        rs.getDate("bookingDate").toLocalDate(),
                        LaundryStartTime.fromTime(rs.getTime("startTime").toLocalTime()),
                        LaundryEndTime.fromTime(rs.getTime("endTime").toLocalTime()),
                        rs.getString("bookedUserId"),
                        rs.getString("laundryRoomId"));

            }
            return Optional.ofNullable(bookedTimeSlot);
        } catch (SQLException e) {
            logger.log(INFO, String.format("DB error in getting time slots for user:%s  %s: ", userId, e));
            return Optional.empty();
        }
    }

    /**
     * Soft deletes the booked time slot by flagging the cancelled time of the booking
     *
     * @param userId
     * @param timeSlotId
     * @return indicates whether the booked time slot was deleted successfully
     */
    @Override
    public boolean delete(String userId, int timeSlotId) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("UPDATE bookingTimeSlot SET deletedAtUnixTime = ?" +
                    " WHERE deletedAtUnixTime = -1 AND bookedUserId = ? AND timeSlotId = ? ");
            stmt.setLong(1, Instant.now().getEpochSecond());
            stmt.setString(2, userId);
            stmt.setInt(3, timeSlotId);

            int val = stmt.executeUpdate();
            if (val == 0) {
                logger.log(WARNING, "No bookings were deleted");
                return false;
            } else if (val == 1) {
                logger.log(INFO, "1 booking is deleted");
                return true;
            } else if (val > 1) {
                logger.log(WARNING, "Multiple bookings were deleted");
                return false;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.log(INFO, "DB error in deleting the booking: " + e);
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            Statement stmt = dbConnection.createStatement();
            return stmt.executeUpdate("DELETE FROM bookingTimeSlot") > 0;
        } catch (SQLException e) {
            logger.log(INFO, "DB error in deleting all the timeslots: " + e);
            return false;
        }
    }
}
