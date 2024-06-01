package org.example;

import java.sql.*;

public class ShowTablesExample {
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

            // Execute query to show tables
            String sqlQuery = "SHOW TABLES";
            rs = stmt.executeQuery(sqlQuery);

            // Process the results
            while (rs.next()) {
                String tableName = rs.getString(1);
                System.out.println(tableName);
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

