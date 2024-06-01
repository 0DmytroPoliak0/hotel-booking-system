package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentDAO {

    public static void addPayment(Payment payment) {
        String sqlQuery = "INSERT INTO Payment (creditCardNumber, bookingID, paymentType, staffID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, payment.getCreditCardNumber());
            preparedStatement.setString(2, payment.getBookingID().toString());
            preparedStatement.setString(3, payment.getPaymentType());
            preparedStatement.setString(4, payment.getStaffID().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePayment(String creditCardNumber, UUID bookingID) {
        String sqlQuery = "DELETE FROM Payment WHERE creditCardNumber = ? AND bookingID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, creditCardNumber);
            preparedStatement.setString(2, bookingID.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Payment getPayment(String creditCardNumber, UUID bookingID) {
        String sqlQuery = "SELECT * FROM Payment WHERE creditCardNumber = ? AND bookingID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, creditCardNumber);
            preparedStatement.setString(2, bookingID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String paymentType = resultSet.getString("paymentType");
                UUID staffID = UUID.fromString(resultSet.getString("staffID"));
                return new Payment(creditCardNumber, bookingID, paymentType, staffID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if payment not found
    }

    public static List<Payment> getAllPayments() {
        List<Payment> paymentList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Payment";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                String creditCardNumber = resultSet.getString("creditCardNumber");
                UUID bookingID = UUID.fromString(resultSet.getString("bookingID"));
                String paymentType = resultSet.getString("paymentType");
                UUID staffID = UUID.fromString(resultSet.getString("staffID"));
                paymentList.add(new Payment(creditCardNumber, bookingID, paymentType, staffID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    // Utility method to print all payments - for demonstration or debugging
    public static void showAllPayments() {
        List<Payment> payments = getAllPayments();
        for (Payment payment : payments) {
            System.out.println("Credit Card Number: " + payment.getCreditCardNumber() + ", Booking ID: " + payment.getBookingID() + ", Payment Type: " + payment.getPaymentType() + ", Staff ID: " + payment.getStaffID());
        }
    }
}
