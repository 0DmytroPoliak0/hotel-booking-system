package org.example;

import java.sql.*;

public class InsertDataExample {
    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/HotelBookingDB";
        String username = "root";
        String password = "DATA_VER_2014";

        // JDBC variables
        Connection conn = null;
        Statement stmt = null;

        try {
            // Connect to the database
            conn = DriverManager.getConnection(url, username, password);

            // Create a statement
            stmt = conn.createStatement();

            // Execute query to insert data
            String sqlQuery = "INSERT INTO User (userID, name, email) VALUES " +
                    "(UUID(), 'Jack Sparrow', 'sparrow@gmail.com')";
            int rowsAffected = stmt.executeUpdate(sqlQuery);

            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
