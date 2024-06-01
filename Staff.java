package org.example;

import java.util.UUID;

public class Staff {
    private UUID staffID;
    private String name;
    private String email;
    private String password;
    private UUID adminID; // This represents the foreign key relationship to Admin

    // Constructor
    public Staff(UUID staffID, String name, String email, String password, UUID adminID) {
        this.staffID = staffID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.adminID = adminID;
    }

    // Getters and Setters
    public UUID getStaffID() {
        return staffID;
    }

    public void setStaffID(UUID staffID) {
        this.staffID = staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public void setAdminID(UUID adminID) {
        this.adminID = adminID;
    }
}
