package quizgame.models.questions;

import java.util.List;

public class MultipleChoiceQuestion implements Question {
    private String questionText;
    private List<String> answerChoices;
    private int correctAnswerIndex;

    public MultipleChoiceQuestion(String questionText, List<String> answerChoices, int correctAnswerIndex) {
        this.questionText = questionText;
        this.answerChoices = answerChoices;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @Override
    public String getQuestionText() {
        return questionText;
    }

    @Override
    public List<String> getAnswerChoices() {
        return answerChoices;
    }

    @Override
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    @Override
    public boolean checkAnswer(int answerIndex) {
        return answerIndex == correctAnswerIndex;
    }
}

