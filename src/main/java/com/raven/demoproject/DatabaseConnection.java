package com.raven.demoproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/revansquad?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "nayem"; // Replace with your DB username
    private static final String PASSWORD = "12345678"; // Replace with your DB password

    String dbUsername = "nayem";
    String dbPassword = "12345678";
    // Method to establish and return the database connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return null if connection fails
        }
    }
}
