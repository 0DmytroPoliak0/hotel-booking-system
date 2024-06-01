package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class HotelDAO {

    public static void addHotel(Hotel hotel) {
        String sqlQuery = "INSERT INTO Hotel (name, address, contact, adminID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, hotel.getName());
            preparedStatement.setString(2, hotel.getAddress());
            preparedStatement.setString(3, hotel.getContact());
            preparedStatement.setString(4, hotel.getAdminID().toString());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    hotel.setHotelID(generatedKeys.getInt(1)); // Set the auto-generated hotelID to the hotel object
                } else {
                    throw new SQLException("Creating hotel failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteHotel(int hotelID) {
        String sqlQuery = "DELETE FROM Hotel WHERE hotelID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, hotelID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Hotel getHotel(int hotelID) {
        String sqlQuery = "SELECT * FROM Hotel WHERE hotelID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, hotelID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String contact = resultSet.getString("contact");
                UUID adminID = UUID.fromString(resultSet.getString("adminID"));
                return new Hotel(hotelID, name, address, contact, adminID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if hotel not found
    }

    public static List<Hotel> getAllHotels() {
        List<Hotel> hotelList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Hotel";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("hotelID");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String contact = resultSet.getString("contact");
                UUID adminID = UUID.fromString(resultSet.getString("adminID"));
                hotelList.add(new Hotel(id, name, address, contact, adminID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelList;
    }

    // Utility method to print all hotels - for demonstration or debugging
    public static void showAllHotels() {
        List<Hotel> hotels = getAllHotels();
        for (Hotel hotel : hotels) {
            System.out.println("Hotel ID: " + hotel.getHotelID() + ", Name: " + hotel.getName() + ", Address: " + hotel.getAddress() + ", Contact: " + hotel.getContact() + ", Admin ID: " + hotel.getAdminID());
        }
    }
    // Method to fetch hotel popularity data
    public static List<HotelPopularity> getHotelsByPopularity() {
        List<HotelPopularity> hotelPopularityList = new ArrayList<>();
        // Updated SQL Query to join through Room table
        String sqlQuery = "SELECT h.hotelID, h.name, COUNT(b.bookingID) AS bookingCount " +
                "FROM Hotel h " +
                "JOIN Room r ON h.hotelID = r.hotelID " + // Join Hotel to Room
                "LEFT JOIN Booking b ON r.roomID = b.roomID " + // Then Room to Booking
                "GROUP BY h.hotelID, h.name " +
                "ORDER BY bookingCount DESC";

        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                int hotelID = resultSet.getInt("hotelID");
                String name = resultSet.getString("name");
                int bookingCount = resultSet.getInt("bookingCount");
                hotelPopularityList.add(new HotelPopularity(hotelID, name, bookingCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelPopularityList;
    }



    // Method to format the hotel popularity data into a string
    public static String getHotelsByPopularityString() {
        StringBuilder sb = new StringBuilder();
        List<HotelPopularity> hotels = getHotelsByPopularity();
        for (HotelPopularity hotel : hotels) {
            sb.append("Hotel ID: ").append(hotel.getHotelID())
                    .append(", Name: ").append(hotel.getName())
                    .append(", Booking Count: ").append(hotel.getBookingCount())
                    .append("\n");
        }
        return sb.toString();
    }

    // Inner class to represent hotel popularity
    public static class HotelPopularity {
        private int hotelID;
        private String name;
        private int bookingCount;

        public HotelPopularity(int hotelID, String name, int bookingCount) {
            this.hotelID = hotelID;
            this.name = name;
            this.bookingCount = bookingCount;
        }

        // Getters
        public int getHotelID() { return hotelID; }
        public String getName() { return name; }
        public int getBookingCount() { return bookingCount; }

        @Override
        public String toString() {
            return "HotelPopularity{" +
                    "hotelID=" + hotelID +
                    ", name='" + name + '\'' +
                    ", bookingCount=" + bookingCount +
                    '}';
        }
    }

    // Utility method to print hotel popularity
    public static void showHotelPopularity() {
        System.out.println(getHotelsByPopularityString());
    }

}
