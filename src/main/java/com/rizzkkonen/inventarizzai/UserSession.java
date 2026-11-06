package com.rizzkkonen.inventarizzai;

public class UserSession {
    private static int userId;
    private static String user;
    private static String role; // Tambahan Role
    private static boolean isLoggedIn = false;

    public static void setSession(int id, String username, String userRole) {
        userId = id;
        user = username;
        role = userRole;
        isLoggedIn = true;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static String getUser() {
        return user;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getRole() {
        return role;
    }

    public static void clearSession() {
        userId = 0;
        user = null;
        role = null;
        isLoggedIn = false;
    }
}