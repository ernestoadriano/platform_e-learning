package com.org.user_service.utils;

public class Validations {

    private Validations() {
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must have at least 8 characters");
        }

        boolean hasLetter = password.matches(".*[A-Za-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSymbol = password.matches(".*[A-Za-z0-9].*");

        if (!hasLetter || !hasDigit || !hasSymbol) {
            throw new RuntimeException("Password must contain letters, digits and symbols");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Invalid email format");
        }
    }

    public static void validateName(String name) {
        if (name == null || name.trim().length() < 2) {
            throw new RuntimeException("Invalid name");
        }
    }
}
