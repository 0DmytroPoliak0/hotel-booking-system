package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HotelPopularityHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            // Get the list of hotels by popularity
            List<HotelDAO.HotelPopularity> hotelPopularityList = HotelDAO.getHotelsByPopularity();
            StringBuilder responseData = new StringBuilder();

            // Building the string to be sent in the response
            for (HotelDAO.HotelPopularity hotel : hotelPopularityList) {
                responseData.append("Hotel ID: ")
                        .append(hotel.getHotelID())
                        .append(", Name: ")
                        .append(hotel.getName())
                        .append(", Booking Count: ")
                        .append(hotel.getBookingCount())
                        .append("\n");
            }

            // Setting headers for the response
            exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"hotel_popularity_report.txt\"");
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");

            byte[] responseBytes = responseData.toString().getBytes(StandardCharsets.UTF_8);

            // Sending the response headers with the length of the response body
            exchange.sendResponseHeaders(200, responseBytes.length);

            // Writing the response data
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            // If the request method is not GET, send a 405 Method Not Allowed response
            exchange.sendResponseHeaders(405, -1); // -1 indicates no response body is being sent
        }
    }
}
