package com.raven.demoproject;

public class SessionManager {
    private static String loggedInUserEmail;

    public static void setLoggedInUserEmail(String email) {
        loggedInUserEmail = email;
    }

    public static String getLoggedInUserEmail() {
        return loggedInUserEmail;
    }

    public static void clearSession() {
        loggedInUserEmail = null;
    }
}
