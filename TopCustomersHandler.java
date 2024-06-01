package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class TopCustomersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            // Get top 3 customers data from UserDAO
            String topCustomersData = UserDAO.getTop3Customers();

            // Suggest to the browser that the content should be downloaded as a file
            exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"top_customers_report.txt\"");
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");

            // Send response
            exchange.sendResponseHeaders(200, topCustomersData.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(topCustomersData.getBytes());
            }
        } else {
            // Method not allowed
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
