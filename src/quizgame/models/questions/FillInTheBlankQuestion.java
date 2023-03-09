package quizgame.models.questions;

public class FillInTheBlankQuestion extends Question {
    private final String answer;

    public FillInTheBlankQuestion(String questionText, String answer) {
        super(questionText);
        this.answer = answer;
    }

    public boolean checkAnswer(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer);
    }
}
