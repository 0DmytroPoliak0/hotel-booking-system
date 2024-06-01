package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RoomTakenHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            LocalDate startDate = null;
            LocalDate endDate = null;

            // Attempt to parse startDate and endDate from the request query parameters
            try {
                String query = exchange.getRequestURI().getQuery();
                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] keyValue = param.split("=");
                        if ("startDate".equals(keyValue[0]) && keyValue.length > 1) {
                            startDate = LocalDate.parse(keyValue[1]);
                        } else if ("endDate".equals(keyValue[0]) && keyValue.length > 1) {
                            endDate = LocalDate.parse(keyValue[1]);
                        }
                    }
                }

                if (startDate == null || endDate == null) {
                    throw new IllegalArgumentException("Missing or incorrect date parameters");
                }
            } catch (DateTimeParseException | IllegalArgumentException e) {
                String response = "Invalid request: " + e.getMessage();
                exchange.sendResponseHeaders(400, response.getBytes().length); // 400 Bad Request
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                return;
            }

            // Fetch room taken data from DAO
            String roomTakenData = BookingDAO.getRoomBookingsByHotelAndDate(startDate, endDate);

            // Set response headers and status code
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, roomTakenData.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(roomTakenData.getBytes());
            }
        } else {
            // If the request method is not GET, send 405 Method Not Allowed
            exchange.sendResponseHeaders(405, 0); // Method Not Allowed, and no response body
        }
    }
}
