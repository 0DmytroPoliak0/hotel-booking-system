package org.example;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reports {
    private UUID reportID;
    private String title;
    private String reportType;
    private String content;
    private LocalDateTime creationDate;
    private UUID bookingID;

    // Constructor for creating new Reports instances
    public Reports(UUID reportID, String title, String reportType, String content, LocalDateTime creationDate, UUID bookingID) {
        this.reportID = reportID;
        this.title = title;
        this.reportType = reportType;
        this.content = content;
        this.creationDate = creationDate;
        this.bookingID = bookingID;
    }

    // Getters and Setters
    public UUID getReportID() {
        return reportID;
    }

    public void setReportID(UUID reportID) {
        this.reportID = reportID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // Assuming creationDate is set automatically to CURRENT_TIMESTAMP by the database,
    // there might not be a need to explicitly set it from the application,
    // but a setter method is provided for completeness.
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public UUID getBookingID() {
        return bookingID;
    }

    public void setBookingID(UUID bookingID) {
        this.bookingID = bookingID;
    }
}
