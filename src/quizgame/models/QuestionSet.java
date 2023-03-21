package quizgame.models;
import quizgame.models.questions.FillInTheBlankQuestion;
import quizgame.models.questions.IdentifyPictureQuestion;
import quizgame.models.questions.MultipleChoiceQuestion;
import quizgame.models.questions.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public void shuffleQuestions() {
        Collections.shuffle(questions);
    }

    public void loadQuestionsFromFile(String filename) throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/questions.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String questionType = parts[0];
                String questionText = parts[1];
                String correctAnswer = parts[parts.length - 1];
                switch (questionType) {
                    case "MultipleChoice" -> {
                        List<String> answerChoices = new ArrayList<>(Arrays.asList(parts).subList(2, parts.length - 1));
                        int correctIndex = Integer.parseInt(correctAnswer);
                        MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(questionText, answerChoices, correctIndex);
                        addQuestion(mcq);
                    }
                    case "FillInTheBlanks" -> {
                        FillInTheBlankQuestion fibq = new FillInTheBlankQuestion(questionText, correctAnswer);
                        addQuestion(fibq);
                    }
                    case "IdentifyPicture" -> {
                        String imagePath = parts[2];
                        List<String> options = new ArrayList<>(Arrays.asList(parts).subList(3, parts.length - 1));
                        int correctOption = Integer.parseInt(parts[parts.length - 1]);
                        IdentifyPictureQuestion ipq = new IdentifyPictureQuestion(questionText, options, correctOption, imagePath);
                        addQuestion(ipq);
                    }
                    default -> throw new RuntimeException("Invalid question type: " + questionType);
                }
            }
        }
    }
}

