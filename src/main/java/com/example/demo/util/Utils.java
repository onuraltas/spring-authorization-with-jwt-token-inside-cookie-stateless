package com.example.demo.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

public class Utils {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPRSTUVYZabcdefghijklmnoprstuvyz";
    private static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public static String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }

    public static boolean isValidEmail(String emailAddress) {
        return emailPattern
                .matcher(emailAddress)
                .matches();
    }

}
