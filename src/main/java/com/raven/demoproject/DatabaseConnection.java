package com.raven.demoproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ravensquad";
    private static final String DB_USERNAME = "nayem";
    private static final String DB_PASSWORD = "12345678";

    // Establish connection to the database
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // Fetch user data by email
    public static ResultSet getUserByEmail(String email) throws Exception {
        String query = "SELECT name, email, phone, gender FROM users2 WHERE email = ?";
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        return preparedStatement.executeQuery();
    }

    // Save user data (this can be used for the signup functionality)
    public static void saveUser(String name, String email, String phone, String gender, String password) throws Exception {
        String query = "INSERT INTO users2 (name, email, phone, gender, password) VALUES (?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, phone);
        preparedStatement.setString(4, gender);
        preparedStatement.setString(5, password);
        preparedStatement.executeUpdate();
    }
}
