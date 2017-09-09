/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import papersystem.MainScreen;

/**
 *
 * @author Abbasi
 * @author Kashan
 */
public class PaperEditor implements ActionListener {

    //Edit Buttons are added to this list..
    ArrayList<JButton> buttonList = null;

    //Generated Questions should be added to this list.. 
    ArrayList<FourFields> printList = null;

    private ArrayList<FourFields> answerList = null;

    //Generated Questions should be added to this list.. 
    ArrayList<JPanel> questionRowList = null;

    //Keeps the reference of the parent to call the printPaper Listener
    PaperPrinter parent = null;

    //Keep a reference to JFrame of Editor so child can enable or disable it
    JFrame editFrame = null;

    String classx = null;

    public static void generateEditMenu(PaperPrinter context, ArrayList<FourFields> printList, ArrayList<FourFields> answerList) {
        PaperEditor editor = new PaperEditor();
        editor.buttonList = new ArrayList<>();
        editor.questionRowList = new ArrayList<>();
        editor.printList = printList;
        editor.answerList = answerList;
        editor.parent = context;
        editor.classx = context.classx;
        //context.parent.setEnabled(false);     This is now done in PaperPrinter

        editor.printGUI();
    }

    private void printGUI() {
        //Set GUI
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        buttonList = new ArrayList<>();

        //Generate the frame...
        JFrame f = new JFrame("Edit Question Paper");
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Paper Editor Closed");
                onClose();
            }
        });
        editFrame = f;
        f.setSize(new Dimension(900, 680));
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));

        //empty space on above top panel buttons...
        f.add(Box.createRigidArea(new Dimension(0, 5)));

        //Panel that contains the print buttons...
        JPanel topPanel = new JPanel();
        topPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JButton printPaper = new JButton("Print Paper");
        printPaper.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.printDocFile();
            }
        });
        topPanel.add(printPaper);
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JButton printAnswer = new JButton("Print Answer Paper");
        topPanel.add(printAnswer);
        printAnswer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.printAnsDocFile();
            }
        });
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JButton saveAnswer = new JButton("Save Answer Paper");
        topPanel.add(saveAnswer);
        saveAnswer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.saveAnswerPaper();
            }
        });
        
        /*
            Sync Button Added 
        */
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JButton syncButton = new JButton("Sync");
        topPanel.add(syncButton);
        syncButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              parent.shuffleQuestions();
              //parent.shuffle(1,6);
              f.dispose();
            }
        });
        
        
        f.add(topPanel);

        //empty space on below top panel buttons...
        f.add(Box.createRigidArea(new Dimension(0, 5)));

        //creating the container where questions will be printed...
        JPanel questionsContainer = new JPanel();
        questionsContainer.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        questionsContainer.setSize(900, 500);
        questionsContainer.setBackground(Color.white);
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        //vertical space at the above first heading
        questionsContainer.add(Box.createRigidArea(new Dimension(5, 19)));

        for (FourFields ff : printList) {
            if (ff.heading || ff.f4.length() > 0) {
                JLabel heading = new JLabel("<html>" + ff.f3 + "</html>");
                heading.setFont(new Font("Tahoma", 1, 11));
                questionsContainer.add(heading);
                //vertical space after the heading
                questionsContainer.add(Box.createRigidArea(new Dimension(5, 19)));
            } else {
                JPanel questionRow = new JPanel();
                questionRow.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                questionRow.setBackground(Color.white);
                questionRow.setLayout(new BoxLayout(questionRow, BoxLayout.X_AXIS));
                //horizontal space before the button
                questionRow.add(Box.createRigidArea(new Dimension(8, 0)));
                JButton editButton = new JButton("EDIT");
                editButton.addActionListener(this);
                buttonList.add(editButton);
                questionRow.add(editButton);

                //horizontal space after the button
                questionRow.add(Box.createRigidArea(new Dimension(8, 0)));

                JPanel question = printQuestionWithImage(ff.f3, parent.classx, parent.subject);
                question.setMaximumSize(question.getPreferredSize());
                questionRow.add(question);

                //Add question Row to the list of questionRows
                questionRowList.add(questionRow);
                questionsContainer.add(questionRow);

                //vertical space after each row
                questionsContainer.add(Box.createRigidArea(new Dimension(5, 19)));
            }
        }
        JScrollPane sp = new JScrollPane(questionsContainer);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        f.add(sp);

        //f.add(new JLabel("ASD3"));
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //first find out which button was clicked...
        int buttonNumber = -2;
        for (int i = 0; i < buttonList.size(); i++) {
            if (e.getSource() == buttonList.get(i)) {
                System.out.println("Button Number: " + i + " Clicked");
                buttonNumber = i;
                break;
            }
        }
        //next find the corresponding question..
        int questionNumber = -1;
        for (int i = 0; i < printList.size(); i++) {
            if (printList.get(i).question) {
                questionNumber++;
            }
            if (questionNumber == buttonNumber) {
                System.out.println(printList.get(i).questionID);
                if (printList.get(i).questionID != -1) {
                    QuestionSelector.createSelector(this,
                            printList.get(i).questionID, buttonNumber);
                }
                break;
            }
        }
    }

    private void onClose() {
        //parent.parent.setAlwaysOnTop(true);
        //parent.parent.setAlwaysOnTop(false);
        parent.parent.setEnabled(true);
    }

    void updateQuestion(int qIDtoReplace, int qIDReplaceWith, int questionRowNumber) {
        //first find corresponding question and replace it..       
        FourFields ff = null;
        int questionNumber = -1;
        for (int i = 0; i < printList.size(); i++) {
            if (printList.get(i).question) {
                questionNumber++;
            }
            if (questionNumber == questionRowNumber) {
                printList.get(i).questionID = qIDReplaceWith;
                printList.get(i).questionID = qIDReplaceWith;
                printList.get(i).f3 = DBManager.getInstance().getQuestion(qIDReplaceWith, classx, parent.subject);
                ff = printList.get(i);
                break;
            }
        }

        questionNumber = -1;
        for (int i = 0; i < answerList.size(); i++) {
            if (answerList.get(i).question) {
                questionNumber++;
            }
            if (questionNumber == questionRowNumber) {
                answerList.get(i).questionID = qIDReplaceWith;
                answerList.get(i).questionID = qIDReplaceWith;
                answerList.get(i).f3 = DBManager.getInstance().getQuestion(qIDReplaceWith, classx, parent.subject);
                answerList.get(i + 1).questionID = qIDReplaceWith;
                answerList.get(i + 1).questionID = qIDReplaceWith;
                answerList.get(i + 1).f3 = DBManager.getInstance().getAnswer(qIDReplaceWith, classx, parent.subject);
                break;
            }
        }

        //next find the corresponding questionRow and update it..
        JPanel questionRow = questionRowList.get(questionRowNumber);
        questionRow.removeAll();
        questionRow.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        questionRow.setBackground(Color.white);
        questionRow.setLayout(new BoxLayout(questionRow, BoxLayout.X_AXIS));
        JButton editButton = new JButton("EDIT");
        editButton.addActionListener(this);
        buttonList.set(questionRowNumber, editButton);
        questionRow.add(editButton);

        //horizontal space after the button
        questionRow.add(Box.createRigidArea(new Dimension(8, 0)));

        JPanel question = printQuestionWithImage(ff.f3, parent.classx, parent.subject);
        question.setMaximumSize(question.getPreferredSize());
        questionRow.add(question);

        editFrame.revalidate();
    }

    static JPanel printQuestionWithImage(String qString, String classx, String subject) {
        JPanel question = new JPanel();
        question.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        question.setBackground(Color.white);
        question.setLayout(new BoxLayout(question, BoxLayout.Y_AXIS));
        for (String s : qString.split("\n")) {
            s=s.trim();
            if (s.startsWith("IMG(")) {
                try {
//                    String number = s.replace("IMG(image", "");
//                    String extension = number.substring(number.indexOf('.'), number.length() - 1);
//                    number = number.substring(0, number.length() - 5);
//                    //get image number..
//                    String filepath = "image" + Integer.parseInt(number);
//                    //find image using subject class address path
//                    filepath = "images/" + parent.classx + "/" + parent.subject + "/" + filepath + extension;
//                    //print image
//                    String imgFile = filepath;//"C:\\VRITI MSSC\\QImages\\G15.1.gif";
                    //String number = s.replace("IMG(image", "");
                    //String extension = number.substring(number.indexOf('.'), number.length() - 1);
                    //number = number.substring(0, number.length() - 5);
                    //get image number..
                    //String filepath = "image" + Integer.parseInt(number);
                    //find image using subject class address path
                    String filepath = "images/" + classx + "/" + subject + "/" + s.substring(0, s.length() - 1).replace("IMG(", "");
                    //print image
                    String imgFile = filepath;//"C:\\VRITI MSSC\\QImages\\G15.1.gif";
                    BufferedImage bimg = ImageIO.read(new File(imgFile));
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();
                    int scaledWidth = getScaledWidth(width, height);
                    int scaledHeight = getScaledHeight(width, height);
                    Image scaledImg = bimg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT);
                    JLabel questionImage = new JLabel();
                    //questionImage.setMaximumSize(new Dimension(700, 2000));

                    questionImage.setIcon(new ImageIcon(scaledImg));
                    question.add(questionImage);
                } catch (IOException ex) {
                    Logger.getLogger(QuestionSelector.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JLabel questionLine = new JLabel("<html><div style=\"width:540px;\">" + s + "</div></html>");
                questionLine.setMaximumSize(new Dimension(700, 2000));
                questionLine.setFont(new Font("Tahoma", 0, 11));
                question.add(questionLine);
            }

        }
        return question;
    }

    private static int getScaledWidth(int width, int height) {
        if (width <= 500) {
            return width;
        }
        return 500;
    }

    private static int getScaledHeight(int width, int height) {
        if (width <= 500) {
            return height;
        }
        System.out.println("height: " + height);
        System.out.println("width: " + width);
        float ratio = (float) height / (float) width;
        System.out.println("ratio: " + ratio);
        System.out.println((int) (500 * ratio));

        return ((int) (500 * ratio));
    }
}
