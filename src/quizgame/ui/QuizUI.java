package quizgame.ui;
import quizgame.models.QuestionSet;
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

    public QuizUI(QuestionSet questionSet) {
        super("Quiz Game");
        this.questionSet = questionSet;
        this.answerButtons = new ArrayList<>();
        this.questionLabel = new JLabel();
        this.nextButton = new JButton("Next");
        this.currentQuestionIndex = 0;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        contentPane.add(questionPanel, BorderLayout.CENTER);

        JPanel answerPanel = new JPanel(new GridLayout(1, 1));
        answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < answerChoices.size(); i++) {
            JRadioButton button = answerButtons.get(i);
            button.setText(answerChoices.get(i));
            button.setVisible(true);
            answerPanel.add(button);
        }

        contentPane.add(answerPanel, BorderLayout.SOUTH);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(nextButton);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);

        nextButton.addActionListener(this);
        loadQuestion(currentQuestionIndex);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            if (currentQuestionIndex < questionSet.getQuestions().size() - 1) {
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Quiz finished");
                dispose();
            }
        }
    }

    private void loadQuestion(int index) {
        Question question = questionSet.getQuestions().get(index);
        questionLabel.setText(question.getQuestionText());

        if (question instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
            List<String> answerChoices = mcq.getAnswerChoices();
            for (int i = 0; i < answerButtons.size(); i++) {
                JRadioButton button = answerButtons.get(i);
                if (i < answerChoices.size()) {
                    button.setText(answerChoices.get(i));
                    button.setVisible(true);
                } else {
                    button.setVisible(false);
                }
            }
            int correctIndex = mcq.getCorrectAnswerIndex();
            answerButtons.get(correctIndex).setActionCommand("correct");
        } else {
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
}

