package finalproject;

import java.io.*;
import java.util.*;

public class TriviaSystem {
    private static final String USER_FILE = "users.txt";
    private static final String QUIZ_FILE = "trivia.txt";

    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Question> questions = new ArrayList<>();

    public static void startTrivia() {
        MainSystem.clearScreen();
        InteractionLogger.println(MainSystem.CYAN + "\n  Loading game data..." + MainSystem.RESET);
        loadUsers();
        loadQuestions();

        boolean inTrivia = true;
        while (inTrivia) {
            MainSystem.printHeader("ULTIMATE TRIVIA QUIZ");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 1 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Login");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 2 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Register (Add Player)");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 3 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " View Leaderboard");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 4 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Admin Panel");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 5 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Back to Main System");
            InteractionLogger.println();
            InteractionLogger.print(MainSystem.YELLOW + "  ➤ Select an option: " + MainSystem.RESET);

            String choice = InteractionLogger.getInput();

            switch (choice) {
                case "1":
                    User loggedInUser = handleLogin();
                    if (loggedInUser != null) playerMenu(loggedInUser);
                    break;
                case "2": handleRegister(); break;
                case "3": showLeaderboard(); break;
                case "4": adminPanel(); break;
                case "5":
                    saveUsers();
                    saveQuestions();
                    inTrivia = false;
                    break;
                default: MainSystem.showError("Invalid choice. Please try again."); break;
            }
        }
    }

    private static void playerMenu(User player) {
        boolean loggedIn = true;
        while (loggedIn) {
            MainSystem.printHeader("PLAYER DASHBOARD");
            InteractionLogger.print(String.format("  Player: %s%-15s%s │ High Score: %s%d%s\n", 
                MainSystem.GREEN + MainSystem.BOLD, player.getUsername(), MainSystem.RESET, 
                MainSystem.YELLOW + MainSystem.BOLD, player.getHighScore(), MainSystem.RESET));
            InteractionLogger.println(MainSystem.CYAN + "  ────────────────────────────────────────────────────" + MainSystem.RESET);
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 1 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Play Game");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 2 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Logout");
            InteractionLogger.println();
            InteractionLogger.print(MainSystem.YELLOW + "  ➤ Choice: " + MainSystem.RESET);

            String choice = InteractionLogger.getInput();
            if (choice.equals("1")) playTrivia(player);
            else if (choice.equals("2")) loggedIn = false;
            else MainSystem.showError("Invalid selection.");
        }
    }

    private static User handleLogin() {
        MainSystem.printHeader("SECURE LOGIN");
        InteractionLogger.print("  Username: ");
        String username = InteractionLogger.getInput();
        InteractionLogger.print("  Password: ");
        String password = InteractionLogger.getInput();

        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                MainSystem.showSuccess("Authentication successful. Welcome, " + u.getUsername() + "!");
                MainSystem.pause();
                return u;
            }
        }
        MainSystem.showError("Login failed. Incorrect credentials.");
        return null;
    }

    private static void handleRegister() {
        MainSystem.printHeader("PLAYER REGISTRATION");
        InteractionLogger.print("  Enter new username: ");
        String username = InteractionLogger.getInput();
        
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                MainSystem.showError("Username already taken!");
                return;
            }
        }

        InteractionLogger.print("  Enter password: ");
        String password = InteractionLogger.getInput();

        if (!username.isEmpty() && !password.isEmpty()) {
            users.add(new User(username, password, 0));
            saveUsers();
            MainSystem.showSuccess("Registered successfully! You can now log in.");
        } else {
            MainSystem.showError("Fields cannot be empty.");
        }
        MainSystem.pause();
    }

    private static void showLeaderboard() {
        MainSystem.printHeader("GLOBAL LEADERBOARD");
        if (users.isEmpty()) {
            InteractionLogger.println(MainSystem.YELLOW + "  No users registered yet." + MainSystem.RESET);
            MainSystem.pause();
            return;
        }

        users.sort((u1, u2) -> Integer.compare(u2.getHighScore(), u1.getHighScore()));

        InteractionLogger.print(String.format(MainSystem.YELLOW + MainSystem.BOLD + "  %-8s │ %-20s │ %-10s\n" + MainSystem.RESET, "RANK", "USERNAME", "SCORE"));
        InteractionLogger.println(MainSystem.PURPLE + "  ─────────┼──────────────────────┼───────────" + MainSystem.RESET);
        for (int i = 0; i < users.size(); i++) {
            String rank = (i == 0) ? "🥇 #1" : (i == 1) ? "🥈 #2" : (i == 2) ? "🥉 #3" : "   #" + (i + 1);
            InteractionLogger.print(String.format("  %-8s │ " + MainSystem.CYAN + "%-20s" + MainSystem.RESET + " │ " + MainSystem.GREEN + "%-10d\n" + MainSystem.RESET, rank, users.get(i).getUsername(), users.get(i).getHighScore()));
        }
        InteractionLogger.println();
        MainSystem.pause();
    }

    private static void playTrivia(User player) {
        if (questions.isEmpty()) {
            MainSystem.showError("No questions available in database.");
            return;
        }

        ArrayList<Question> playList = new ArrayList<>(questions);
        Collections.shuffle(playList);
        int totalQuestions = Math.min(10, playList.size());
        
        for (int i = 0; i < totalQuestions; i++) playList.get(i).setUserAnswer("");

        Stack<Question> backStack = new Stack<>();
        Stack<Question> nextStack = new Stack<>();

        for (int i = totalQuestions - 1; i >= 0; i--) nextStack.push(playList.get(i));

        Question currentQuestion = nextStack.pop();
        boolean takingQuiz = true;

        while (takingQuiz) {
            MainSystem.clearScreen();
            
            // Format Question nicely
            InteractionLogger.println(MainSystem.CYAN + "  ╔═════════════════════════════════════════════════════════════════╗");
            String qText = currentQuestion.getQuestionText();
            if (qText.length() > 61) {
                InteractionLogger.print(String.format("  ║ Q: %-60.60s ║\n", qText.substring(0, 60)));
                InteractionLogger.print(String.format("  ║    %-60.60s ║\n", qText.substring(60)));
            } else {
                InteractionLogger.print(String.format("  ║ Q: %-60.60s ║\n", qText));
            }
            
            InteractionLogger.println("  ╠═════════════════════════════════════════════════════════════════╣");
            // [FIXED] Changed spacing from %-27.27s to %-28.28s to perfectly align the borders!
            InteractionLogger.print(String.format("  ║ A) %-28.28s B) %-28.28s ║\n", currentQuestion.getOptionA(), currentQuestion.getOptionB()));
            InteractionLogger.print(String.format("  ║ C) %-28.28s D) %-28.28s ║\n", currentQuestion.getOptionC(), currentQuestion.getOptionD()));
            InteractionLogger.println("  ╚═════════════════════════════════════════════════════════════════╝" + MainSystem.RESET);

            if (!currentQuestion.getUserAnswer().isEmpty()) {
                InteractionLogger.println(MainSystem.GREEN + "\n  [ Selected Answer: " + currentQuestion.getUserAnswer() + " ]" + MainSystem.RESET);
            } else {
                InteractionLogger.println(MainSystem.YELLOW + "\n  [ Unanswered ]" + MainSystem.RESET);
            }

            InteractionLogger.println(MainSystem.PURPLE + "\n  Actions: " + MainSystem.WHITE + "[A/B/C/D]" + MainSystem.PURPLE + " Select │ " + MainSystem.WHITE + "[NEXT]" + MainSystem.PURPLE + " Skip │ " + MainSystem.WHITE + "[BACK]" + MainSystem.PURPLE + " │ " + MainSystem.WHITE + "[SUBMIT]" + MainSystem.RESET);
            InteractionLogger.print(MainSystem.YELLOW + "  ➤ Input: " + MainSystem.RESET);

            String input = InteractionLogger.getInput().toUpperCase();

            switch (input) {
                case "A": case "B": case "C": case "D":
                    currentQuestion.setUserAnswer(input);
                    if (!nextStack.isEmpty()) {
                        backStack.push(currentQuestion);
                        currentQuestion = nextStack.pop();
                    } else {
                        InteractionLogger.println(MainSystem.GREEN + "\n  [★] Answer saved! You are on the last question. Type SUBMIT to finish." + MainSystem.RESET);
                        try { Thread.sleep(1500); } catch (Exception e) {}
                    }
                    break;
                case "NEXT":
                    if (!nextStack.isEmpty()) {
                        backStack.push(currentQuestion);
                        currentQuestion = nextStack.pop();
                    } else {
                        InteractionLogger.println(MainSystem.RED + "  [!] You are on the last question." + MainSystem.RESET);
                        try { Thread.sleep(1000); } catch (Exception e) {}
                    }
                    break;
                case "BACK":
                    if (!backStack.isEmpty()) {
                        nextStack.push(currentQuestion);
                        currentQuestion = backStack.pop();
                    } else {
                        InteractionLogger.println(MainSystem.RED + "  [!] You are on the first question." + MainSystem.RESET);
                        try { Thread.sleep(1000); } catch (Exception e) {}
                    }
                    break;
                case "SUBMIT":
                    InteractionLogger.print(MainSystem.YELLOW + "  ➤ Submit quiz and finish? (YES/NO): " + MainSystem.RESET);
                    if (InteractionLogger.getInput().toUpperCase().equals("YES")) {
                        backStack.push(currentQuestion); 
                        takingQuiz = false;
                    }
                    break;
                default:
                    InteractionLogger.println(MainSystem.RED + "  [!] Invalid command." + MainSystem.RESET);
                    try { Thread.sleep(1000); } catch (Exception e) {}
                    break;
            }
        }

        int score = 0;
        ArrayList<Question> allAnswered = new ArrayList<>();
        allAnswered.addAll(backStack);
        allAnswered.addAll(nextStack);

        for (Question q : allAnswered) {
            if (q.getUserAnswer().equals(q.getCorrectAnswer().toUpperCase())) score++;
        }

        MainSystem.printHeader("QUIZ RESULTS");
        InteractionLogger.println(MainSystem.GREEN + MainSystem.BOLD + "  FINAL SCORE: " + score + " / " + totalQuestions + MainSystem.RESET);

        if (score > player.getHighScore()) {
            InteractionLogger.println(MainSystem.YELLOW + MainSystem.BOLD + "\n  ★ CONGRATULATIONS! NEW HIGH SCORE! ★" + MainSystem.RESET);
            player.setHighScore(score);
            saveUsers();
        }
        MainSystem.pause();
    }

    private static void adminPanel() {
        MainSystem.printHeader("SYSTEM ADMINISTRATOR");
        InteractionLogger.print("  Admin Password: ");
        if (!InteractionLogger.getInput().equals("admin")) {
            MainSystem.showError("Access Denied.");
            return;
        }

        boolean inAdmin = true;
        while (inAdmin) {
            MainSystem.printHeader("ADMIN CONTROL PANEL");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 1 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " List All Users");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 2 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Delete User");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 3 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " List All Questions");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 4 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Add New Question");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 5 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Delete Question");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 6 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Search Question DB");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 7 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Log Out Admin");
            InteractionLogger.println();
            InteractionLogger.print(MainSystem.YELLOW + "  ➤ Execute Command: " + MainSystem.RESET);

            String choice = InteractionLogger.getInput();

            switch (choice) {
                case "1":
                    MainSystem.printHeader("REGISTERED USERS");
                    for (User u : users) InteractionLogger.println("  • " + MainSystem.CYAN + u.getUsername() + MainSystem.RESET + " (" + u.getHighScore() + " pts)");
                    MainSystem.pause();
                    break;
                case "2":
                    InteractionLogger.print("\n  Target Username for Deletion: ");
                    String delUser = InteractionLogger.getInput();
                    boolean found = users.removeIf(u -> u.getUsername().equalsIgnoreCase(delUser));
                    if (found) {
                        MainSystem.showSuccess("User permanently deleted.");
                        saveUsers();
                    } else MainSystem.showError("User record not found.");
                    break;
                case "3":
                    MainSystem.printHeader("QUESTION DATABASE");
                    for (int i = 0; i < questions.size(); i++) InteractionLogger.println("  [" + i + "] " + questions.get(i).getQuestionText());
                    MainSystem.pause();
                    break;
                case "4":
                    InteractionLogger.print("\n  Prompt Text: "); String qt = InteractionLogger.getInput();
                    InteractionLogger.print("  Option A:    "); String a = InteractionLogger.getInput();
                    InteractionLogger.print("  Option B:    "); String b = InteractionLogger.getInput();
                    InteractionLogger.print("  Option C:    "); String c = InteractionLogger.getInput();
                    InteractionLogger.print("  Option D:    "); String d = InteractionLogger.getInput();
                    InteractionLogger.print("  Correct Letter (A/B/C/D): "); String ans = InteractionLogger.getInput().toUpperCase();
                    
                    if (qt.isEmpty() || ans.isEmpty()) {
                        MainSystem.showError("Invalid formatting. Operation cancelled.");
                    } else {
                        questions.add(new Question(qt, a, b, c, d, ans));
                        saveQuestions();
                        MainSystem.showSuccess("Question saved to DB.");
                    }
                    break;
                case "5":
                    int index = MainSystem.getValidInt("\n  Enter Question Index ID to delete: ");
                    if (index >= 0 && index < questions.size()) {
                        questions.remove(index);
                        saveQuestions();
                        MainSystem.showSuccess("Question purged.");
                    } else {
                        MainSystem.showError("Index out of bounds. Question not found.");
                    }
                    break;
                case "6":
                    InteractionLogger.print("\n  Search DB Query: ");
                    String kw = InteractionLogger.getInput().toLowerCase();
                    for (Question q : questions) {
                        if (q.getQuestionText().toLowerCase().contains(kw)) InteractionLogger.println("  Match: " + q.getQuestionText());
                    }
                    MainSystem.pause();
                    break;
                case "7": inAdmin = false; break;
                default: MainSystem.showError("Unrecognized command."); break;
            }
        }
    }

    private static void loadUsers() {
        users.clear();
        try (Scanner s = new Scanner(new File(USER_FILE))) {
            while (s.hasNextLine()) {
                String[] p = s.nextLine().split(",");
                if (p.length == 3) {
                    try { users.add(new User(p[0], p[1], Integer.parseInt(p[2]))); } catch (NumberFormatException e) {}
                }
            }
        } catch (Exception e) {}
    }

    private static void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User u : users) pw.println(u.getUsername() + "," + u.getPassword() + "," + u.getHighScore());
        } catch (Exception e) {}
    }

    private static void loadQuestions() {
        questions.clear();
        try (Scanner s = new Scanner(new File(QUIZ_FILE))) {
            while (s.hasNextLine()) {
                String[] p = s.nextLine().split("\\|");
                if (p.length == 6) questions.add(new Question(p[0], p[1], p[2], p[3], p[4], p[5]));
            }
        } catch (Exception e) {}
    }

    private static void saveQuestions() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(QUIZ_FILE))) {
            for (Question q : questions) pw.println(q.getQuestionText() + "|" + q.getOptionA() + "|" + q.getOptionB() + "|" + q.getOptionC() + "|" + q.getOptionD() + "|" + q.getCorrectAnswer());
        } catch (Exception e) {}
    }
}