package com.library.management.utils;

public final class Utils {

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                // Windows command to clear the screen
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Unix/Linux/Mac command to clear the screen
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.err.println("Error while clearing the terminal." + e.getMessage());
        }
    }
}
