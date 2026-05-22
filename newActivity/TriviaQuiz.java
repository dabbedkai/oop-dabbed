
package newActivity;
import java.io.*;
import java.util.*;

public class TriviaQuiz {

private static final String USER_FILE = "users.txt";
private static final String QUIZ_FILE = "trivia.txt";

private static ArrayList<User> users = new ArrayList<>();
private static ArrayList<Question> questions = new ArrayList<>();

private static Scanner scanner = new Scanner(System.in);

public static void main(String[] args) {
    System.out.println("Loading game data...");
    loadUsers();
    loadQuestions();

    boolean systemRunning = true;
    while (systemRunning) {
        System.out.println("\n=== ULTIMATE TRIVIA QUIZ ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            User loggedInUser = handleLogin();
            if (loggedInUser != null) {
                mainMenu(loggedInUser);
            }
        } else if (choice.equals("2")) {
            handleRegister();
        } else if (choice.equals("3")) {
            System.out.println("Thanks for playing!");
            systemRunning = false;
        } else {
            System.out.println("Invalid choice. Try again.");
        }
    }
}

private static void mainMenu(User player) {
    boolean loggedIn = true;
    while (loggedIn) {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("Player: " + player.getUsername() + " | High Score: " + player.getHighScore());
        System.out.println("1. Play Game");
        System.out.println("2. Logout");
        System.out.print("Choice: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            playTrivia(player);
        } else if (choice.equals("2")) {
            loggedIn = false;
        } else {
            System.out.println("Invalid selection.");
        }
    }
}

private static void playTrivia(User player) {
    System.out.println("\nSetting up your quiz...");

    ArrayList<Question> playList = new ArrayList<>(questions);
    Collections.shuffle(playList);

    int totalQuestions = Math.min(10, playList.size());
    List<Question> selectedQuestions = playList.subList(0, totalQuestions);

    for (Question q : selectedQuestions) {
        q.setUserAnswer("");
    }

    Stack<Question> backStack = new Stack<>();
    Stack<Question> nextStack = new Stack<>();

    for (int i = selectedQuestions.size() - 1; i >= 0; i--) {
        nextStack.push(selectedQuestions.get(i));
    }

    if (nextStack.isEmpty()) {
        System.out.println("No questions available to play.");
        return;
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
            System.out.println("Your current answer: " + currentQuestion.getUserAnswer());
        } else {
            System.out.println("You have not answered this question yet.");
        }

        System.out.println("\nInstructions:");
        System.out.println("- Type A, B, C, or D to save your answer.");
        System.out.println("- Type NEXT to move forward.");
        System.out.println("- Type BACK to return to the previous question.");
        System.out.println("- Type SUBMIT to finish the quiz and see your score.");
        System.out.print("\nWhat would you like to do? ");

        String input = scanner.nextLine().trim().toUpperCase();

        if (input.equals("A") || input.equals("B") || input.equals("C") || input.equals("D")) {
            currentQuestion.setUserAnswer(input);
            System.out.println("Great! Answer recorded. Type NEXT to continue.");
        } else if (input.equals("NEXT")) {
            if (!nextStack.isEmpty()) {
                backStack.push(currentQuestion);
                currentQuestion = nextStack.pop();
            } else {
                System.out.println("You are already at the last question. Type SUBMIT when ready.");
            }
        } else if (input.equals("BACK")) {
            if (!backStack.isEmpty()) {
                nextStack.push(currentQuestion);
                currentQuestion = backStack.pop();
            } else {
                System.out.println("You are already at the first question.");
            }
        } else if (input.equals("SUBMIT")) {
            System.out.print("Are you sure you want to submit your quiz? (YES/NO): ");
            String confirm = scanner.nextLine().trim().toUpperCase();
            if (confirm.equals("YES")) {
                takingQuiz = false;
            }
        } else {
            System.out.println("Invalid input. Please read the instructions and try again.");
        }
    }

    int score = 0;
    for (Question q : selectedQuestions) {
        if (q.getUserAnswer().equals(q.getCorrectAnswer().toUpperCase())) {
            score++;
        }
    }

    System.out.println("\n--- GAME OVER ---");
    System.out.println("You scored: " + score + " out of " + totalQuestions);

    if (score > player.getHighScore()) {
        System.out.println("Awesome job! You reached a new High Score!");
        player.setHighScore(score);
        saveUsers();
    }
}

private static void loadQuestions() {
    File file = new File(QUIZ_FILE);

    if (!file.exists()) {
        createDefaultQuestions();
    }

    try (Scanner fileScanner = new Scanner(file)) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split("\\|");

            if (parts.length == 6) {
                Question q = new Question(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim());
                questions.add(q);
            }
        }
    } catch (Exception e) {
        System.out.println("Failed to read trivia file.");
    }
}

private static void createDefaultQuestions() {
    try (PrintWriter pw = new PrintWriter(new FileWriter(QUIZ_FILE))) {
        pw.println("What is the capital of France?|Berlin|London|Paris|Madrid|C");
        pw.println("What is 5 + 5?|10|12|15|8|A");
        pw.println("Which language are we using?|Python|C++|Java|HTML|C");
        pw.println("How many legs does a spider have?|6|8|10|12|B");
        System.out.println("Automatically created 'trivia.txt' with sample questions!");
    } catch (IOException e) {
        System.out.println("Could not create trivia.txt.");
    }
}

private static void loadUsers() {
    File file = new File(USER_FILE);
    if (!file.exists()) return;

    try (Scanner fileScanner = new Scanner(file)) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length == 3) {
                users.add(new User(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim())));
            }
        }
    } catch (Exception e) {
        System.out.println("Failed to load users.");
    }
}

private static void saveUsers() {
    try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
        for (User u : users) {
            pw.println(u.getUsername() + "," + u.getPassword() + "," + u.getHighScore());
        }
    } catch (IOException e) {
        System.out.println("Could not save users.");
    }
}

private static User handleLogin() {
    System.out.print("Username: ");
    String username = scanner.nextLine().trim();
    System.out.print("Password: ");
    String password = scanner.nextLine().trim();

    for (User u : users) {
        if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
            System.out.println("\nLogin Successful! Welcome, " + u.getUsername() + "!");
            return u;
        }
    }
    System.out.println("Login failed! Please check your credentials.");
    return null;
}

private static void handleRegister() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine().trim();
    System.out.print("Enter password: ");
    String password = scanner.nextLine().trim();

    if (username.isEmpty() || password.isEmpty()) return;

    users.add(new User(username, password, 0));
    saveUsers();
    System.out.println("Registered successfully! You can now log in.");
}
}