package quizgame.models.questions;

import java.util.Arrays;
import java.util.List;

public class FillInTheBlankQuestion extends Question {
    private String answer;

    public FillInTheBlankQuestion(String questionText, String answer) {
        super(questionText);
        this.answer = answer;
    }

    public boolean checkAnswer(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer);
    }

    public List<String> getAnswerChoices() {
        return Arrays.asList("");
    }
    
}
