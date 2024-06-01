package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.sun.net.httpserver.HttpExchange;
import java.net.URLDecoder;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/topCustomers", new TopCustomersHandler());
        server.createContext("/hotelPopularity", new HotelPopularityHandler());
        server.createContext("/roomTaken", new RoomTakenHandler()); // Registering RoomTakenHandler
        server.createContext("/bookRoom", new BookingFormHandler());
        server.createContext("/addHotel", new AddHotelHandler());
        server.createContext("/api/hotels", new FetchHotelsHandler());
        server.createContext("/register", new RegisterHandler());
        server.createContext("/user", new UserPageHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/", new StaticFileHandler("resources"));
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
    }


    static class TopCustomersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Get top 3 customers data from UserDAO
                String topCustomersData = UserDAO.getTop3Customers();

                // Send response
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, topCustomersData.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(topCustomersData.getBytes());
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class HotelPopularityHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Get the list of hotels by popularity
                String hotelPopularityData = HotelDAO.getHotelsByPopularityString(); // Implement this method in HotelDAO

                // Send response
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, hotelPopularityData.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(hotelPopularityData.getBytes());
                }
            } else {
                // If the request method is not GET, respond with 405 Method Not Allowed
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class RoomTakenHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                LocalDate startDate = null;
                LocalDate endDate = null;
                // Parse query parameters for startDate and endDate
                String query = exchange.getRequestURI().getQuery();
                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] pair = param.split("=");
                        if (pair[0].equals("startDate") && pair.length > 1) {
                            startDate = LocalDate.parse(pair[1]);
                        } else if (pair[0].equals("endDate") && pair.length > 1) {
                            endDate = LocalDate.parse(pair[1]);
                        }
                    }
                }

                if (startDate == null || endDate == null) {
                    // If startDate or endDate are missing, return an error
                    String response = "Missing or incorrect date parameters.";
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }

                // Fetch room taken data from DAO
                String roomTakenData = BookingDAO.getRoomBookingsByHotelAndDate(startDate, endDate);

                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(200, roomTakenData.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(roomTakenData.getBytes());
                }
            } else {
                // If the request method is not GET, return method not allowed
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }
    }


    static class UserPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String email = "user@example.com"; // Adjust this to actually retrieve user's email from session or request
                List<Booking> bookings = BookingDAO.getBookingsByEmail(email);

                StringBuilder bookingDetailsHtml = new StringBuilder();
                for (Booking booking : bookings) {
                    String roomType = RoomDAO.getRoomTypeByRoomID(booking.getRoomID());
                    bookingDetailsHtml.append(String.format(
                            "<div class='bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6'>" +
                                    "<dt class='text-sm font-medium text-gray-500'>Room Type</dt>" +
                                    "<dd class='mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2'>%s</dd>" +
                                    "<dt class='text-sm font-medium text-gray-500'>Dates</dt>" +
                                    "<dd class='mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2'>%s to %s</dd>" +
                                    "</div>",
                            roomType,
                            booking.getCheckInDate().toString(),
                            booking.getCheckOutDate().toString()
                    ));
                }

                String response = "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head><meta charset='UTF-8'><title>User Bookings</title></head>" +
                        "<body><h2>Your Booking Details</h2>" +
                        "<div id='bookingDetails'>" + bookingDetailsHtml + "</div>" +
                        "</body></html>";

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                // Method Not Allowed
                exchange.sendResponseHeaders(405, 0);
            }
        }
    }



    static class AddHotelHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();
                Map<String, String> parsedFormData = parseFormData(formData);

                // Now, parsedFormData contains your form fields
                // Implement addHotelToDatabase() to insert the hotel into your database
                boolean success = addHotelToDatabase(parsedFormData);

                String response = success ? "Hotel added successfully!" : "Failed to add hotel.";
                exchange.sendResponseHeaders(success ? 200 : 500, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Handle method not allowed
            }
        }

        private Map<String, String> parseFormData(String formData) {
            Map<String, String> parsedFormData = new HashMap<>();
            // Implementation for parsing the form data and filling the map
            return parsedFormData;
        }

        private boolean addHotelToDatabase(Map<String, String> hotelData) {
            // Implementation to insert hotel into the database
            return true; // Return true if successful, false otherwise
        }
    }

    static class FetchHotelsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Hotel> hotels = HotelDAO.getAllHotels(); // Assume this method exists and fetches hotels

                // Manually build JSON
                StringBuilder jsonResponse = new StringBuilder("[");
                for (int i = 0; i < hotels.size(); i++) {
                    Hotel hotel = hotels.get(i);
                    jsonResponse.append(String.format(
                            "{\"hotelID\": \"%d\", \"name\": \"%s\", \"address\": \"%s\", \"contact\": \"%s\", \"adminID\": \"%s\"}",
                            hotel.getHotelID(), escapeJson(hotel.getName()), escapeJson(hotel.getAddress()), escapeJson(hotel.getContact()), hotel.getAdminID().toString()
                    ));
                    if (i < hotels.size() - 1) {
                        jsonResponse.append(",");
                    }
                }
                jsonResponse.append("]");

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.toString().getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(jsonResponse.toString().getBytes());
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        private String escapeJson(String value) {
            return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b")
                    .replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        }
    }







    static class StaticFileHandler implements HttpHandler {
        private final String root;

        public StaticFileHandler(String root) {
            this.root = root;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            File file = new File(root, path);

            if (file.isFile()) {
                exchange.sendResponseHeaders(200, file.length());
                try (OutputStream os = exchange.getResponseBody(); FileInputStream fs = new FileInputStream(file)) {
                    final byte[] buffer = new byte[0x10000];
                    int count;
                    while ((count = fs.read(buffer)) >= 0) {
                        os.write(buffer, 0, count);
                    }
                }
            } else {
                String response = "404 (Not Found)\n";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }


    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Parse HTTP request
            String requestLine = in.readLine();
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            if (method.equals("POST") && path.equals("/login")) {
                // Read request body to get email and password
                StringBuilder requestBody = new StringBuilder();
                String line;
                while (!(line = in.readLine()).isEmpty()) {
                    requestBody.append(line).append("\r\n");
                }

                // Extract email and password from request body
                String[] bodyParts = requestBody.toString().split("&");
                String email = URLDecoder.decode(bodyParts[0].split("=")[1], "UTF-8");
                String password = URLDecoder.decode(bodyParts[1].split("=")[1], "UTF-8");

                // Authenticate user
                if (UserDAO.authenticate(email, password)) {
                    // Generate session ID
                    String sessionId = generateSessionId();

                    // Save session ID in database
                    SessionDAO.saveSession(UUID.fromString(sessionId), email);

                    // Send response with session ID
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/plain");
                    out.println("Set-Cookie: session=" + sessionId);
                    out.println();
                    out.println("Login successful. Welcome, " + email);
                } else {
                    // Invalid credentials
                    out.println("HTTP/1.1 401 Unauthorized");
                    out.println("Content-Type: text/plain");
                    out.println();
                    out.println("Invalid email or password");
                }
            } else {
                // Invalid request
                out.println("HTTP/1.1 400 Bad Request");
                out.println("Content-Type: text/plain");
                out.println();
                out.println("Bad Request");
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateSessionId() {
        // Generate random session ID (e.g., using UUID)
        return UUID.randomUUID().toString();
    }



    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b")
                .replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    // For User Registration
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Retrieve form data
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder formData = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    formData.append(line);
                }
                br.close();
                // Parse form data (assuming it's in the format "username=xxx&email=xxx&password=xxx")
                String[] params = formData.toString().split("&");
                String username = params[0].split("=")[1];
                String email = URLDecoder.decode(params[1].split("=")[1], "UTF-8");
                String password = params[2].split("=")[1];

                // Create a new user object
                UUID userID = UUID.randomUUID();
               // User user = new User(userID, username, email, password);

                // Add the user to the database using UserDAO
              //  UserDAO.addUser(user);

                // Send response back to the client
                String response = "Registration successful";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        }
    }

}
