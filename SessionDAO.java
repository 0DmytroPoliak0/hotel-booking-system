package org.example;// SessionDAO.java
// Implement methods to manage user sessions in the database

import org.example.DatabaseDAO;

import java.sql.*;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SessionDAO {
    private static final String SESSION_TABLE = "sessions";

    public static String createSession(String email) {
        String sessionId = generateSessionId();
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + SESSION_TABLE + " (session_id, email) VALUES (?, ?)")) {
            stmt.setString(1, sessionId);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessionId;
    }

    public static String getEmailBySessionId(String sessionId) {
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT email FROM " + SESSION_TABLE + " WHERE session_id = ?")) {
            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public static void saveSession(UUID sessionId, String email) {
        try (Connection conn = DatabaseDAO.getConnection()) {
            // Prepare SQL statement to insert session into the database
            String sqlQuery = "INSERT INTO sessions (session_id, email) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, sessionId.toString());
                preparedStatement.setString(2, email);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
