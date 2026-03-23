package com.library.controller;

import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }
}
