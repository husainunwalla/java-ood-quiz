package quizgame.ui;

import quizgame.models.QuestionSet;
import quizgame.models.questions.FillInTheBlankQuestion;
import quizgame.models.questions.MultipleChoiceQuestion;
import quizgame.models.questions.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizUI extends JFrame implements ActionListener {
    private QuestionSet questionSet;
    private List<JRadioButton> answerButtons;
    private JLabel questionLabel;
    private JButton nextButton;
    private int currentQuestionIndex;
    private int selectedAnswerIndex;
    private int score;
    private JLabel scoreLabel;
    private JTextField answerField;

    public QuizUI(QuestionSet questionSet) {
        super("Quiz Game");
        this.questionSet = questionSet;
        this.answerButtons = new ArrayList<>();
        this.questionLabel = new JLabel();
        this.nextButton = new JButton("Next");
        this.currentQuestionIndex = 0;
        this.score = 0;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        contentPane.add(questionPanel, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel(new FlowLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.add(answerPanel, BorderLayout.CENTER);

        answerField = new JTextField(20);
        answerPanel.add(answerField);

        for (int i = 0; i < 4; i++) {
            JRadioButton button = new JRadioButton();
            answerButtons.add(button);
            answerPanel.add(button);
            button.addActionListener(this);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(nextButton);
        scoreLabel = new JLabel("Score: " + score);
        buttonPanel.add(scoreLabel);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);


        nextButton.addActionListener(this);
        loadQuestion(currentQuestionIndex);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            clearSelection();
            Question question = questionSet.getQuestions().get(currentQuestionIndex);
            checkAnswer(question);
            scoreLabel.setText("Score: " + score);
            if (currentQuestionIndex < questionSet.getQuestions().size() - 1) {
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Quiz finished. Score: " + score);
                dispose();
            }
        }else{
            selectedAnswerIndex = answerButtons.indexOf(e.getSource());
        }
    }

    private void loadQuestion(int index) {
        Question question = questionSet.getQuestions().get(index);
        questionLabel.setText(question.getQuestionText());
        if (question instanceof MultipleChoiceQuestion) {
            answerField.setVisible(false);
            List<String> answerChoices = ((MultipleChoiceQuestion) question).getAnswerChoices();
            ButtonGroup buttonGroup = new ButtonGroup();
            for (int i = 0; i < answerButtons.size(); i++) {
                JRadioButton button = answerButtons.get(i);
                if (i < answerChoices.size()) {
                    button.setText(answerChoices.get(i));
                    button.setVisible(true);
                    buttonGroup.add(button);
                } else {
                    button.setVisible(false);
                }
            }
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
            int correctIndex = mcq.getCorrectAnswerIndex();
            answerButtons.get(correctIndex).setActionCommand("correct");
        }else if (question instanceof FillInTheBlankQuestion) {
            answerField.setVisible(true);
            FillInTheBlankQuestion fitb = (FillInTheBlankQuestion) question;
            //Disable Multiple choice buttons
            for (JRadioButton button : answerButtons) {
                button.setVisible(false);
            }
        }
        clearSelection();
    }


    private void clearSelection() {
        for (JRadioButton button : answerButtons) {
            button.setSelected(false);
        }
    }

    private void checkAnswer(Question question){
        if (question instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
            int correctIndex = mcq.getCorrectAnswerIndex();
            if (selectedAnswerIndex == correctIndex) score++;
        } else if (question instanceof FillInTheBlankQuestion) {
            FillInTheBlankQuestion fib = (FillInTheBlankQuestion) question;
            if (fib.checkAnswer(answerField.getText())) score ++;
        }
    }
}
