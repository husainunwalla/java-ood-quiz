package quizgame.main;
import javax.swing.*;
import quizgame.models.QuestionSet;
import quizgame.ui.QuizUI;

public class Main {
    public static void main(String[] args) {
        // Load the question set from a file
        QuestionSet questionSet = new QuestionSet();
        try {
            questionSet.loadQuestionsFromFile("res/questions.txt");
            questionSet.shuffleQuestions();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading question set: " + e.getMessage());
            return;
        }

        // Create the quiz UI and show it
        QuizUI quizUI = new QuizUI(questionSet);
        quizUI.setVisible(true);
    }
}
