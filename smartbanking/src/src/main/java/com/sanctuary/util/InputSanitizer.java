package com.sanctuary.util;

import com.sanctuary.exception.InvalidAmountException;

public class InputSanitizer {

    public static String sanitizeName(String name) throws InvalidAmountException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidAmountException("Name cannot be empty.");
        }
        String cleaned = name.trim().replaceAll("[^a-zA-Z0-9 .'-]", "");
        if (cleaned.isEmpty()) {
            throw new InvalidAmountException("Name contains no valid characters.");
        }
        return cleaned;
    }

    public static String sanitizePhone(String phone) throws InvalidAmountException {
        String cleaned = phone.replaceAll("[^0-9]", "");
        if (cleaned.length() < 7 || cleaned.length() > 15) {
            throw new InvalidAmountException("Invalid phone number length.");
        }
        return cleaned;
    }

    public static String sanitizeEmail(String email) throws InvalidAmountException {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidAmountException("Invalid email format.");
        }
        return email.toLowerCase();
    }

    public static int validatePin(int pin) throws InvalidAmountException {
        if (pin < 1000 || pin > 9999) {
            throw new InvalidAmountException("PIN must be a 4-digit number between 1000 and 9999.");
        }
        return pin;
    }

    public static double validatePositiveAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive.");
        }
        if (amount > 999_999_999.99) {
            throw new InvalidAmountException("Amount exceeds maximum allowed (999,999,999.99).");
        }
        return Math.round(amount * 100.0) / 100.0;
    }
}
