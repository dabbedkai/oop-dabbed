package finalproject;

public class User {
    private String username;
    private String password;
    private int highScore;

    // constructor
    public User(String username, String password, int highScore) {
        this.username = username;
        this.password = password;
        this.highScore = highScore;
    }

    // getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
}