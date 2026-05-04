package finalproject;
import java.util.Scanner;

public class MainSystem {
    // scanner for the whole app
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        // main loop to choose between the two projects
        while (running) {
            System.out.println("\n=================================");
            System.out.println("   FINAL PROJECT MAIN SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Ultimate Trivia Game");
            System.out.println("2. Service Center (Grocery/Movie/Music)");
            System.out.println("3. Exit System");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // launch the trivia game
                    TriviaSystem.startTrivia();
                    break;
                case "2":
                    // launch the old array/file project
                    ServiceSystem.startService();
                    break;
                case "3":
                    System.out.println("exiting system. saving all data...");
                    running = false;
                    break;
                default:
                    System.out.println("invalid choice. please try again.");
                    break;
            }
        }
    }
}