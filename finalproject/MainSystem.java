package finalproject;

public class MainSystem {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String RED = "\u001B[1;31m";
    public static final String GREEN = "\u001B[1;32m";
    public static final String YELLOW = "\u001B[1;33m";
    public static final String BLUE = "\u001B[1;34m";
    public static final String PURPLE = "\u001B[1;35m";
    public static final String CYAN = "\u001B[1;36m";
    public static final String WHITE = "\u001B[1;37m"; // <--- Added this line!

    public static void main(String[] args) {
        InteractionLogger.init(); 
        boolean running = true;

        while (running) {
            printHeader("FINAL PROJECT MAIN SYSTEM");
            InteractionLogger.println("  " + CYAN + "[" + BOLD + " 1 " + RESET + CYAN + "]" + RESET + " Ultimate Trivia Game");
            InteractionLogger.println("  " + CYAN + "[" + BOLD + " 2 " + RESET + CYAN + "]" + RESET + " Service Center (Grocery/Movie/Music)");
            InteractionLogger.println("  " + CYAN + "[" + BOLD + " 3 " + RESET + CYAN + "]" + RESET + " Exit System");
            InteractionLogger.println();
            InteractionLogger.print(YELLOW + "  ➤ Choose an option: " + RESET);

            String choice = InteractionLogger.getInput();

            switch (choice) {
                case "1": TriviaSystem.startTrivia(); break;
                case "2": ServiceSystem.startService(); break;
                case "3":
                    clearScreen();
                    InteractionLogger.println(GREEN + "\n  [✔] Exiting system. Data saved. Check 'receipt.txt' for logs. Goodbye!\n" + RESET);
                    running = false;
                    break;
                default:
                    showError("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
        }
    }

    public static int getValidInt(String prompt) {
        while (true) {
            InteractionLogger.print(prompt);
            String input = InteractionLogger.getInput();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showError("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static void clearScreen() {
        InteractionLogger.clearScreen();
    }

    public static void printHeader(String title) {
        clearScreen();
        InteractionLogger.println(PURPLE + BOLD + "  ╔════════════════════════════════════════════════════╗" + RESET);
        int spaces = (52 - title.length()) / 2;
        int extra = (title.length() % 2 == 0) ? 0 : 1;
        String format = PURPLE + BOLD + "  ║" + RESET + CYAN + BOLD + "%" + (spaces == 0 ? "" : spaces + "s") + "%s%" + (spaces + extra == 0 ? "" : (spaces + extra) + "s") + PURPLE + BOLD + "║\n" + RESET;
        InteractionLogger.print(String.format(format, "", title, ""));
        InteractionLogger.println(PURPLE + BOLD + "  ╚════════════════════════════════════════════════════╝" + RESET);
        InteractionLogger.println();
    }

    public static void pause() {
        InteractionLogger.print(BLUE + "\n  Press [ENTER] to continue..." + RESET);
        InteractionLogger.getInput();
    }

    public static void showError(String msg) {
        InteractionLogger.println(RED + "\n  [!] " + msg + RESET);
        pause();
    }

    public static void showSuccess(String msg) {
        InteractionLogger.println(GREEN + "\n  [✔] " + msg + RESET);
    }
}