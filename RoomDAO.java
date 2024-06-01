package org.example;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;


public class RoomDAO {

    public static String getRoomTypeByRoomID(int roomID) {
        String roomType = null;
        String sql = "SELECT roomType FROM Room WHERE roomID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, roomID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                roomType = resultSet.getString("roomType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomType;
    }

    public static boolean updateRoomAvailability(int roomID, boolean newAvailability) {
        String sql = "UPDATE Room SET availability = ? WHERE roomID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setBoolean(1, newAvailability);
            preparedStatement.setInt(2, roomID);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a room is available.
     *
     * @param roomID The ID of the room to check.
     * @return true if the room is available, false otherwise.
     */
    public static boolean isRoomAvailable(int roomID, LocalDate checkInDate, LocalDate checkOutDate) {
        String sql = "SELECT COUNT(*) AS booking_count FROM Booking " +
                "WHERE roomID = ? AND NOT (checkOutDate <= ? OR checkInDate >= ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, roomID);
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            preparedStatement.setDate(3, Date.valueOf(checkOutDate));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // If booking_count is 0, then no bookings overlap with the desired dates, and the room is available
                return resultSet.getInt("booking_count") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Consider the room unavailable if an error occurs
    }

    /**
     * Finds available rooms by room type for a given date range.
     * @param roomType The type of room to search for.
     * @param checkInDate The check-in date for availability.
     * @param checkOutDate The check-out date for availability.
     * @return A list of room IDs that match the room type and are available for booking.
     */
    public static List<Integer> findAvailableRoomsByType(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Integer> availableRooms = new ArrayList<>();
        String sql = "SELECT r.roomID FROM Room r WHERE r.roomType = ? AND r.roomID NOT IN (" +
                "SELECT b.roomID FROM Booking b WHERE NOT (b.checkOutDate <= ? OR b.checkInDate >= ?))";

        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, roomType);
            preparedStatement.setDate(2, Date.valueOf(checkInDate));
            preparedStatement.setDate(3, Date.valueOf(checkOutDate));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                availableRooms.add(resultSet.getInt("roomID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableRooms;
    }

}
