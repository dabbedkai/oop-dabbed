package finalproject;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InteractionLogger {
    private static final String LOG_FILE = "receipt.txt";
    private static final String ANSI_REGEX = "\u001B\\[[;\\d]*m|\033\\[H\033\\[2J|\033\\[3J";
    public static Scanner scanner = new Scanner(System.in);

    public static void init() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, false))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println("████████████████████████████████████████████████████████████");
            pw.println("              SYSTEM INTERACTION RECEIPT / LOG              ");
            pw.println("              Session Start: " + timestamp + "             ");
            pw.println("████████████████████████████████████████████████████████████\n");
        } catch (IOException e) {
            System.out.println("Warning: Unable to initialize receipt logger.");
        }
    }

    private static void log(String text) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.print(text.replaceAll(ANSI_REGEX, ""));
        } catch (IOException e) {}
    }

    public static void print(String msg) {
        System.out.print(msg);
        log(msg);
    }

    public static void println(String msg) {
        System.out.println(msg);
        log(msg + "\n");
    }

    public static void println() {
        System.out.println();
        log("\n");
    }

    public static String getInput() {
        String input = scanner.nextLine().trim();
        log(input + "\n");
        return input;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();

        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        } catch (Exception e) {}
        
        log("\n[==================== SCREEN CLEARED ====================]\n\n");
    }
}