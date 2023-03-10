package quizgame.ui;

import quizgame.models.QuestionSet;
import quizgame.models.questions.FillInTheBlankQuestion;
import quizgame.models.questions.IdentifyPictureQuestion;
import quizgame.models.questions.MultipleChoiceQuestion;
import quizgame.models.questions.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizUI extends JFrame implements ActionListener {
    private final QuestionSet questionSet;
    private final List<JRadioButton> answerButtons;
    private final JLabel questionLabel;
    private final JButton nextButton;
    private int currentQuestionIndex;
    private int selectedAnswerIndex;
    private int score;
    private final JLabel scoreLabel;
    private final JTextField answerField;
    private final JPanel contentPane;
    private final JLabel titleLabel;
    private boolean startGame;
    private final JPanel namePanel;
    private Image image;
    private  JLabel imageLabel;

    public QuizUI(QuestionSet questionSet) {
        super("Quiz Game");
        this.questionSet = questionSet;
        this.answerButtons = new ArrayList<>();
        this.questionLabel = new JLabel();
        this.nextButton = new JButton("Next");
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.answerField = new JTextField(20);
        this.scoreLabel = new JLabel("Score: " + score);
        this.contentPane = new JPanel(new BorderLayout());
        this.startGame = false;
        this.namePanel = new JPanel(new FlowLayout());
        this.titleLabel = new JLabel("Welcome to the Quiz Game!");
        this.selectedAnswerIndex = 0;
        this.image = null;
        this.imageLabel = null;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);


        setContentPane(contentPane);

        //Title Page and name box
        JLabel nameLabel = new JLabel("Enter your name:");
        JTextField nameField = new JTextField(10);
        namePanel.add(nameLabel);
        namePanel.add(nameField);



        contentPane.add(titleLabel, BorderLayout.NORTH);
        contentPane.add(namePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(nextButton);
        buttonPanel.add(scoreLabel);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);


        nextButton.addActionListener(this);
    }

    private void loadGame(){
        contentPane.remove(namePanel);
        contentPane.remove(titleLabel);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        contentPane.add(questionPanel, BorderLayout.NORTH);

        Image img = new ImageIcon("https://cdn.britannica.com/73/114973-050-2DC46083/Midtown-Manhattan-Empire-State-Building-New-York.jpg").getImage();

        // Create label with image icon
        JLabel label = new JLabel(new ImageIcon(img));
        contentPane.add(label, BorderLayout.CENTER);


        JPanel answerPanel = new JPanel(new FlowLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.add(answerPanel, BorderLayout.CENTER);


        answerPanel.add(answerField);

        for (int i = 0; i < 4; i++) {
            JRadioButton button = new JRadioButton();
            answerButtons.add(button);
            answerPanel.add(button);
            button.addActionListener(this);
        }

        loadQuestion(currentQuestionIndex);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            if(!this.startGame){
                loadGame();
                this.startGame = true;
                return;
            }
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
        if (question instanceof MultipleChoiceQuestion mcq) {
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
            int correctIndex = mcq.getCorrectAnswerIndex();
            answerButtons.get(correctIndex).setActionCommand("correct");
        }else if (question instanceof FillInTheBlankQuestion) {
            answerField.setVisible(true);
            //Disable Multiple choice buttons
            for (JRadioButton button : answerButtons) {
                button.setVisible(false);
            }
        }
        if (question instanceof IdentifyPictureQuestion ipq) {
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
            int correctIndex = ipq.getCorrectAnswerIndex();
            answerButtons.get(correctIndex).setActionCommand("correct");

        }
        clearSelection();
    }


    private void clearSelection() {
        for (JRadioButton button : answerButtons) {
            button.setSelected(false);
        }
    }

    private void checkAnswer(Question question){
        if (question instanceof MultipleChoiceQuestion mcq) {
            int correctIndex = mcq.getCorrectAnswerIndex();
            if (selectedAnswerIndex == correctIndex) score++;
        } else if (question instanceof FillInTheBlankQuestion fib) {
            if (fib.checkAnswer(answerField.getText())) score ++;
        }
    }
}
