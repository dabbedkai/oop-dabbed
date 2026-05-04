package finalproject;

import java.io.*;
import java.util.*;

public class TriviaSystem {
    private static final String USER_FILE = "users.txt";
    private static final String QUIZ_FILE = "trivia.txt";

    // arraylists to hold our data
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Question> questions = new ArrayList<>();

    public static void startTrivia() {
        System.out.println("loading game data...");
        loadUsers();
        loadQuestions();

        boolean inTrivia = true;
        while (inTrivia) {
            System.out.println("\n=== ULTIMATE TRIVIA QUIZ ===");
            System.out.println("1. Login");
            System.out.println("2. Register (Add Player)");
            System.out.println("3. View Leaderboard");
            System.out.println("4. Admin Panel (Manage Users/Questions)");
            System.out.println("5. Back to Main System");
            System.out.print("Select an option: ");

            String choice = MainSystem.scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    User loggedInUser = handleLogin();
                    if (loggedInUser != null) {
                        playerMenu(loggedInUser);
                    }
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    showLeaderboard();
                    break;
                case "4":
                    adminPanel();
                    break;
                case "5":
                    saveUsers();
                    saveQuestions();
                    inTrivia = false;
                    break;
                default:
                    System.out.println("invalid choice.");
                    break;
            }
        }
    }

    // --- PLAYER STUFF ---

    private static void playerMenu(User player) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- PLAYER MENU ---");
            System.out.println("Welcome, " + player.getUsername() + " | High Score: " + player.getHighScore());
            System.out.println("1. Play Game");
            System.out.println("2. Logout");
            System.out.print("Choice: ");

            String choice = MainSystem.scanner.nextLine().trim();

            if (choice.equals("1")) {
                playTrivia(player);
            } else if (choice.equals("2")) {
                loggedIn = false;
            } else {
                System.out.println("invalid selection.");
            }
        }
    }

    private static User handleLogin() {
        System.out.print("Username: ");
        String username = MainSystem.scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = MainSystem.scanner.nextLine().trim();

        // loop to find user
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                System.out.println("login successful!");
                return u;
            }
        }
        System.out.println("login failed. wrong username or password.");
        return null;
    }

    private static void handleRegister() {
        System.out.print("Enter new username: ");
        String username = MainSystem.scanner.nextLine().trim();
        
        // check if user already exists
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                System.out.println("username already taken!");
                return;
            }
        }

        System.out.print("Enter password: ");
        String password = MainSystem.scanner.nextLine().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            users.add(new User(username, password, 0));
            saveUsers();
            System.out.println("registered successfully!");
        } else {
            System.out.println("fields cannot be empty.");
        }
    }

    private static void showLeaderboard() {
        if (users.isEmpty()) {
            System.out.println("no users yet.");
            return;
        }

        // basic bubble sort to sort users by high score (descending)
        for (int i = 0; i < users.size() - 1; i++) {
            for (int j = 0; j < users.size() - i - 1; j++) {
                if (users.get(j).getHighScore() < users.get(j + 1).getHighScore()) {
                    // swap
                    User temp = users.get(j);
                    users.set(j, users.get(j + 1));
                    users.set(j + 1, temp);
                }
            }
        }

        System.out.println("\n--- LEADERBOARD ---");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getUsername() + " - " + users.get(i).getHighScore() + " pts");
        }
    }

    // --- GAMEPLAY MODULE (IMPLEMENTS STACKS) ---

    private static void playTrivia(User player) {
        if (questions.isEmpty()) {
            System.out.println("no questions available. tell admin to add some.");
            return;
        }

        System.out.println("\nsetting up your quiz...");

        // copy and shuffle questions so no repeats and it's random
        ArrayList<Question> playList = new ArrayList<>(questions);
        Collections.shuffle(playList);

        int totalQuestions = Math.min(10, playList.size());
        
        // clear previous answers just in case
        for (int i = 0; i < totalQuestions; i++) {
            playList.get(i).setUserAnswer("");
        }

        // use stacks for navigation
        Stack<Question> backStack = new Stack<>();
        Stack<Question> nextStack = new Stack<>();

        // push questions to nextStack in reverse so the first question is on top
        for (int i = totalQuestions - 1; i >= 0; i--) {
            nextStack.push(playList.get(i));
        }

        Question currentQuestion = nextStack.pop();
        boolean takingQuiz = true;

        while (takingQuiz) {
            System.out.println("\n====================================");
            System.out.println("Question: " + currentQuestion.getQuestionText());
            System.out.println("A) " + currentQuestion.getOptionA());
            System.out.println("B) " + currentQuestion.getOptionB());
            System.out.println("C) " + currentQuestion.getOptionC());
            System.out.println("D) " + currentQuestion.getOptionD());
            System.out.println("====================================");

            if (!currentQuestion.getUserAnswer().isEmpty()) {
                System.out.println("your current answer: " + currentQuestion.getUserAnswer());
            }

            System.out.println("\n[A/B/C/D] to answer | [NEXT] forward | [BACK] previous | [SUBMIT] finish");
            System.out.print("Action: ");

            String input = MainSystem.scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "A": case "B": case "C": case "D":
                    currentQuestion.setUserAnswer(input);
                    System.out.println("answer recorded.");
                    break;
                case "NEXT":
                    if (!nextStack.isEmpty()) {
                        backStack.push(currentQuestion);
                        currentQuestion = nextStack.pop();
                    } else {
                        System.out.println("this is the last question. type SUBMIT to finish.");
                    }
                    break;
                case "BACK":
                    if (!backStack.isEmpty()) {
                        nextStack.push(currentQuestion);
                        currentQuestion = backStack.pop();
                    } else {
                        System.out.println("you are on the first question.");
                    }
                    break;
                case "SUBMIT":
                    System.out.print("submit quiz? (YES/NO): ");
                    String confirm = MainSystem.scanner.nextLine().trim().toUpperCase();
                    if (confirm.equals("YES")) {
                        // push the current one back so it gets graded
                        backStack.push(currentQuestion); 
                        takingQuiz = false;
                    }
                    break;
                default:
                    System.out.println("invalid input.");
                    break;
            }
        }

        // calculate score
        int score = 0;
        // combine all answered questions from stacks
        ArrayList<Question> allAnswered = new ArrayList<>();
        allAnswered.addAll(backStack);
        allAnswered.addAll(nextStack);

        for (Question q : allAnswered) {
            if (q.getUserAnswer().equals(q.getCorrectAnswer().toUpperCase())) {
                score++;
            }
        }

        System.out.println("\n--- GAME OVER ---");
        System.out.println("You scored: " + score + " out of " + totalQuestions);

        if (score > player.getHighScore()) {
            System.out.println("new high score! awesome job.");
            player.setHighScore(score);
            saveUsers();
        }
    }

    // --- ADMIN MODULE (CRUD FOR USERS & QUESTIONS) ---

    private static void adminPanel() {
        System.out.println("\n-- ADMIN LOGIN --");
        System.out.print("Admin Password (type 'admin'): ");
        String pass = MainSystem.scanner.nextLine();
        
        if (!pass.equals("admin")) {
            System.out.println("access denied.");
            return;
        }

        boolean inAdmin = true;
        while (inAdmin) {
            System.out.println("\n=== ADMIN PANEL ===");
            System.out.println("1. List All Users");
            System.out.println("2. Delete User");
            System.out.println("3. List All Questions");
            System.out.println("4. Add Question");
            System.out.println("5. Delete Question");
            System.out.println("6. Search Question");
            System.out.println("7. Back");
            System.out.print("Choice: ");

            String choice = MainSystem.scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    for (User u : users) {
                        System.out.println("- " + u.getUsername() + " (Score: " + u.getHighScore() + ")");
                    }
                    break;
                case "2":
                    System.out.print("enter username to delete: ");
                    String delUser = MainSystem.scanner.nextLine();
                    boolean userFound = false;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getUsername().equalsIgnoreCase(delUser)) {
                            users.remove(i);
                            System.out.println("user deleted.");
                            saveUsers();
                            userFound = true;
                            break;
                        }
                    }
                    if (!userFound) System.out.println("user not found.");
                    break;
                case "3":
                    for (int i = 0; i < questions.size(); i++) {
                        System.out.println("[" + i + "] " + questions.get(i).getQuestionText());
                    }
                    break;
                case "4":
                    System.out.print("Question Text: ");
                    String qt = MainSystem.scanner.nextLine();
                    System.out.print("Option A: "); String a = MainSystem.scanner.nextLine();
                    System.out.print("Option B: "); String b = MainSystem.scanner.nextLine();
                    System.out.print("Option C: "); String c = MainSystem.scanner.nextLine();
                    System.out.print("Option D: "); String d = MainSystem.scanner.nextLine();
                    System.out.print("Correct Answer (A/B/C/D): "); String ans = MainSystem.scanner.nextLine();
                    
                    questions.add(new Question(qt, a, b, c, d, ans));
                    saveQuestions();
                    System.out.println("question added.");
                    break;
                case "5":
                    System.out.print("enter question index to delete: ");
                    try {
                        int index = Integer.parseInt(MainSystem.scanner.nextLine());
                        questions.remove(index);
                        saveQuestions();
                        System.out.println("question deleted.");
                    } catch (Exception e) {
                        System.out.println("invalid index.");
                    }
                    break;
                case "6":
                    System.out.print("enter keyword: ");
                    String keyword = MainSystem.scanner.nextLine().toLowerCase();
                    for (Question q : questions) {
                        if (q.getQuestionText().toLowerCase().contains(keyword)) {
                            System.out.println("found: " + q.getQuestionText());
                        }
                    }
                    break;
                case "7":
                    inAdmin = false;
                    break;
                default:
                    System.out.println("invalid.");
                    break;
            }
        }
    }

    // --- FILE I/O ---

    private static void loadUsers() {
        users.clear();
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) return;
            
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String[] parts = fileReader.nextLine().split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            }
            fileReader.close();
        } catch (Exception e) {
            System.out.println("could not load users.");
        }
    }

    private static void saveUsers() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE));
            for (User u : users) {
                pw.println(u.getUsername() + "," + u.getPassword() + "," + u.getHighScore());
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("error saving users.");
        }
    }

    private static void loadQuestions() {
        questions.clear();
        try {
            File file = new File(QUIZ_FILE);
            if (!file.exists()) return;

            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String[] parts = fileReader.nextLine().split("\\|");
                if (parts.length == 6) {
                    questions.add(new Question(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
            fileReader.close();
        } catch (Exception e) {
            System.out.println("could not load questions.");
        }
    }

    private static void saveQuestions() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(QUIZ_FILE));
            for (Question q : questions) {
                pw.println(q.getQuestionText() + "|" + q.getOptionA() + "|" + q.getOptionB() + "|" + 
                           q.getOptionC() + "|" + q.getOptionD() + "|" + q.getCorrectAnswer());
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("error saving questions.");
        }
    }
}