package quizgame.models.questions;

import java.util.List;

public class IdentifyPictureQuestion extends MultipleChoiceQuestion {
    private String pictureUrl;

    public IdentifyPictureQuestion(String questionText, List<String> answerChoices, int correctIndex, String pictureUrl) {
        super(questionText, answerChoices, correctIndex);
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
