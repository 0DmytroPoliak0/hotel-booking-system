package org.example;

import java.util.List; // Add this import statement

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

import java.util.UUID;
public class Main {
    public static void main(String[] args) {

      //  UserDAO.showUsersTable();
        UserDAO.getTop3Customers();
      //  UserDAO.addUser();

    }
}

/*
*
        // Generate a random UUID for the user ID
        UUID userID = UUID.randomUUID();

        // Create a new User object
        String username = "newUser";
        String email = "newuser@example.com";
        User newUser = new User(userID, username, email);

        // Insert the new user into the database if the user with the same email doesn't already exist
        if (!userExists(email)) {
            UserDAO.addUser(newUser);
        } else {
            System.out.println("User with email " + email + " already exists.");
        }

        // Retrieve the users from the database
        List<User> users = UserDAO.getUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static boolean userExists(String email) {
        List<User> users = UserDAO.getUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
* */
