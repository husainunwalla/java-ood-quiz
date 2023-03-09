package quizgame.models.questions;

import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private final List<String> answerChoices;
    private final int correctAnswerIndex;

    public MultipleChoiceQuestion(String questionText, List<String> answerChoices, int correctAnswerIndex) {
        super(questionText);
        this.answerChoices = answerChoices;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public List<String> getAnswerChoices() {
        return answerChoices;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}

