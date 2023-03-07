package quizgame.models.questions;
import java.util.List;

public interface Question {

    /**
     * Returns the text of the question.
     */
    public String getQuestionText();

    /**
     * Returns the list of possible answers for the question.
     */
    public List<String> getAnswerChoices();

    /**
     * Returns the index of the correct answer in the answer choices list.
     */
    public int getCorrectAnswerIndex();

    /**
     * Checks if the given answer is correct for this question.
     * @param answerIndex the index of the answer to check
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(int answerIndex);
}
