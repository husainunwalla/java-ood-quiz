package quizgame.ui;

import quizgame.models.QuestionSet;
import quizgame.models.questions.FillInTheBlankQuestion;
import quizgame.models.questions.IdentifyPictureQuestion;
import quizgame.models.questions.MultipleChoiceQuestion;
import quizgame.models.questions.Question;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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
    private final JTextField nameField;
    private boolean startGame;
    private final JPanel namePanel;
    private final JLabel imageLabel;
    private String userName;

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
        this.nameField = new JTextField(10);
        this.selectedAnswerIndex = 0;
        this.imageLabel = new JLabel();
        this.userName = "Name not entered";

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);


        setContentPane(contentPane);

        //Title Page and name box
        JLabel nameLabel = new JLabel("Enter your name:");

        namePanel.add(nameLabel);
        namePanel.add(nameField);


        titleLabel.setFont(new Font("Serif", Font.PLAIN, 40));
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
        String filePath = "scores.txt"; // Specify the file path
        File file = new File(filePath);
        if (e.getSource() == nextButton) {
            if(!this.startGame){
                loadGame();
                this.startGame = true;
                this.userName = this.nameField.getText();
                return;
            }
            clearSelection();
            Question question = questionSet.getQuestions().get(currentQuestionIndex);
            checkAnswer(question);
            scoreLabel.setText("Score: " + score + " out of " + (currentQuestionIndex + 1));
            if (currentQuestionIndex < questionSet.getQuestions().size() - 1) {
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Quiz finished. \nScore: " + score + " out of " + questionSet.getQuestions().size());
                final String scoreFileEntry = this.userName + "," + this.score + "\n";
                try {
                    FileWriter writer = new FileWriter(file, true); // Use FileWriter to append to file
                    writer.write(scoreFileEntry);
                    writer.close();
                    System.out.println("Content appended to file successfully");
                }catch (IOException error) {
                    //exception handling left as an exercise for the reader
                    System.out.println("Could not write score to File " + error);
                }
                // Read scores from file
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        String[] parts = line.split(",");
                        dataset.addValue(Integer.parseInt(parts[1]), "Score", parts[0]);
                    }
                } catch (IOException ScoreError) {
                    ScoreError.printStackTrace();
                }

                // Create chart
                JFreeChart chart = ChartFactory.createBarChart(
                        "Scores",          // chart title
                        "Player",          // x axis label
                        "Score",           // y axis label
                        dataset,           // data
                        PlotOrientation.VERTICAL,
                        true,              // legend
                        true,              // tooltips
                        false              // urls
                );

                // Display chart
                ChartFrame frame = new ChartFrame("Scores", chart);
                frame.pack();
                frame.setVisible(true);
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
            try {
                URL imageUrl = new URL(ipq.getPictureUrl());
                Image image = ImageIO.read(imageUrl);
                ImageIcon imageIcon = new ImageIcon(image);
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Add the image label to the content pane
            contentPane.add(imageLabel, BorderLayout.EAST);

        }
        clearSelection();
    }


    private void clearSelection() {
        for (JRadioButton button : answerButtons) {
            button.setSelected(false);
        }
    }

    private void checkAnswer(Question question){
        if (question instanceof IdentifyPictureQuestion){
            this.contentPane.remove(this.imageLabel);
        }
        if (question instanceof MultipleChoiceQuestion mcq) {
            int correctIndex = mcq.getCorrectAnswerIndex();
            if (selectedAnswerIndex == correctIndex) score++;
        } else if (question instanceof FillInTheBlankQuestion fib) {
            if (fib.checkAnswer(answerField.getText())) score ++;
        }
    }
}
