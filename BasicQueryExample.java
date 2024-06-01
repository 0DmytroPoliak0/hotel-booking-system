package org.example;
import java.sql.*;
import java.util.UUID;


public class BasicQueryExample {
    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/HotelBookingDB";
        String username = "root";
        String password = "DATA_VER_2014";

        // JDBC variables
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            conn = DriverManager.getConnection(url, username, password);

            // Create a statement
            stmt = conn.createStatement();

            // Execute query
            String sqlQuery = "SELECT * FROM User";
            rs = stmt.executeQuery(sqlQuery);

            // Process the results
            while (rs.next()) {
                // Retrieve data from the result set
                String idString = rs.getString("userID");
                UUID id = UUID.fromString(idString);
                String name = rs.getString("name");

                // Do something with the data
                System.out.println("ID: " + id + ", Name: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
