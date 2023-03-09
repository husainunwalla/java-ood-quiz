package quizgame.models;
import quizgame.models.questions.FillInTheBlankQuestion;
import quizgame.models.questions.MultipleChoiceQuestion;
import quizgame.models.questions.Question;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionSet {
    private final List<Question> questions;

    public QuestionSet() {
        questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void loadQuestionsFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String questionType = parts[0];
                String questionText = parts[1];
                List<String> answerChoices = new ArrayList<>(Arrays.asList(parts).subList(2, parts.length - 1));
                String correctAnswer = parts[parts.length - 1];
                switch (questionType) {
                    case "MultipleChoice" -> {
                        int correctIndex = Integer.parseInt(correctAnswer);
                        MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(questionText, answerChoices, correctIndex);
                        addQuestion(mcq);
                    }
                    case "FillInTheBlanks" -> {
                        FillInTheBlankQuestion fibq = new FillInTheBlankQuestion(questionText, correctAnswer);
                        addQuestion(fibq);
                    }
                    default -> throw new RuntimeException("Invalid question type: " + questionType);
                }
            }
        }
    }
}

