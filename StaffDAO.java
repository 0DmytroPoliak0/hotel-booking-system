package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffDAO {

    public static void addStaff(Staff staff) {
        String sqlQuery = "INSERT INTO Staff (staffID, name, email, password, adminID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, staff.getStaffID().toString());
            preparedStatement.setString(2, staff.getName());
            preparedStatement.setString(3, staff.getEmail());
            preparedStatement.setString(4, staff.getPassword());
            preparedStatement.setString(5, staff.getAdminID().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStaff(UUID staffID) {
        String sqlQuery = "DELETE FROM Staff WHERE staffID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, staffID.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Staff getStaff(UUID staffID) {
        String sqlQuery = "SELECT * FROM Staff WHERE staffID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, staffID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                UUID adminID = UUID.fromString(resultSet.getString("adminID"));
                return new Staff(staffID, name, email, password, adminID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if staff not found
    }

    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Staff";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                UUID staffID = UUID.fromString(resultSet.getString("staffID"));
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                UUID adminID = resultSet.getString("adminID") != null ? UUID.fromString(resultSet.getString("adminID")) : null;
                staffList.add(new Staff(staffID, name, email, password, adminID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    // Utility method to print all staff members - for demonstration or debugging
    public static void showAllStaff() {
        List<Staff> staff = getAllStaff();
        for (Staff member : staff) {
            System.out.println("Staff ID: " + member.getStaffID() + ", Name: " + member.getName() + ", Email: " + member.getEmail() + ", Admin ID: " + member.getAdminID());
        }
    }
}
