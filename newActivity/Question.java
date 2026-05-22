package newActivity;

class Question {
    private String questionText;
    private String optionA, optionB, optionC, optionD;
    private String correctAnswer;
    private String userAnswer;

    public Question(String qText, String a, String b, String c, String d, String answer) {
        this.questionText = qText;
        this.optionA = a;
        this.optionB = b;
        this.optionC = c;
        this.optionD = d;
        this.correctAnswer = answer;
        this.userAnswer = "";
    }

    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
}