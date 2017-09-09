/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the parent.
 */
package papersystem.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 *
 * @author Abbasi
 * @author Kashan
 */
public class QuestionSelector implements ActionListener {

    //Select Buttons are added to this list..
    ArrayList<JButton> buttonList = new ArrayList<>();
    //vars to hold info about shownn questions
    ArrayList<String> questionsList = new ArrayList<>();
    ArrayList<Integer> qIDList = new ArrayList<>();
    //data for finding the questions to show..
    ArrayList<String> chapters = null;
    ArrayList<String> addedQuestionIDs = null;
    //holds id of question to replace...
    int questionID = -1;
    //calling editor instance, for using listener..
    PaperEditor parent;
    //this is the question row number passed from PaperEditor.. 
    //for purposes of calling updateQuestion Listener
    int questionRowNumber = -1;
    //frame in which every thing will be placed...
    JFrame frame;
    JPanel questionsContainer = new JPanel();

    String classx = null;

    public static void createSelector(PaperEditor context, int questionID,
            int questionRowNumber) {
        QuestionSelector selector = new QuestionSelector();
        selector.parent = context;
        selector.questionID = questionID;
        selector.questionRowNumber = questionRowNumber;
        selector.chapters = context.parent.chapters;
        selector.addedQuestionIDs = PaperPrinter.addedQuestionIDs;
        context.editFrame.setEnabled(false);
        selector.classx = context.classx;

        System.out.println("Added ID:");
        for (String a : selector.addedQuestionIDs) {
            System.out.println("Added ID: " + a);
        }
//        No Need to generate question here... drop down menu change in gui calls the listener and questions are automatically generated...
//        //generate questions for all selected chapters
//        selector.generateQuestions(selector.chapters);

        //call gui creator...
        selector.printGUI();

    }

    public void printGUI() {

        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        frame = new JFrame("Question Selector");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Question Selector Closed");
                onClose();
            }
        });
        frame.setSize(new Dimension(900, 680));
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(Box.createRigidArea(new Dimension(0, 5)));

        //Panel that contains the print buttons...
        JPanel topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(2000, 0));
        //topPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        //JLabel heading = new JLabel("Please select one of the available questions.");
        JLabel select = new JLabel("<html><b>Select Chapter: </b><html>");

        JComboBox chapterSelector = new JComboBox();

        chapterSelector.addItem("All Selected Chapters");
        for (String c : chapters) {
            chapterSelector.addItem(c);
        }
        chapterSelector.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    chapterChanged(chapterSelector.getSelectedItem().toString());

                }
            }
        });
        if (chapters.size() > 0) {
            chapterSelector.setSelectedIndex(1);
        }
        //topPanel.add(heading);
        topPanel.add(select);
        topPanel.add(chapterSelector);
        frame.add(topPanel);

        //empty space on below top panel buttons...
        frame.add(Box.createRigidArea(new Dimension(0, 5)));

        generateQuestionsContainer();
        JScrollPane sp = new JScrollPane(questionsContainer);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);        
        frame.add(sp);

        //f.add(new JLabel("ASD3"));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
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
        for (int i = 0; i < qIDList.size(); i++) {
            questionNumber++;
            if (questionNumber == buttonNumber) {
                System.out.println(qIDList.get(i));
                frame.dispose();
                onClose();
                parent.updateQuestion(questionID, qIDList.get(i), questionRowNumber);
                break;
            }
        }
    }

    private void generateQuestions(ArrayList<String> chapters) {
        //first clear all previous questions...
        questionsList.clear();
        qIDList.clear();
        //find questions from database...
        String questionType = DBManager.getInstance().getQuestionTypeByQuestionID(questionID, classx, parent.parent.subject);
        int subID = parent.parent.subjectID;
        qIDList.addAll(DBManager.getInstance().getAllNotAddedQuestionsIDs(questionType, subID, chapters, addedQuestionIDs, classx));

        questionsList = DBManager.getInstance().getQuestionsByIDs(qIDList, classx, parent.parent.subject);
        System.out.println(questionsList.size() + " ----- " + qIDList.size());

        for (int a = 0; a < qIDList.size(); a++) {
            int compare = questionsList.get(a).compareTo(DBManager.getInstance().getQuestion(qIDList.get(a), classx, parent.parent.subject));
            if (compare != 0) {
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
        System.out.println("HUIHUIHUI   " + qIDList.size() + " Questiosn available");
    }

    private void onClose() {
        parent.editFrame.setAlwaysOnTop(true);
        parent.editFrame.setAlwaysOnTop(false);
        parent.editFrame.setEnabled(true);
    }

    private void chapterChanged(String selectedChapter) {
        System.out.println("New selected chapter is " + selectedChapter);
        //check if not the first time...
        if (questionsContainer == null) {
            return;
        }
        frame.remove(questionsContainer);
        if (selectedChapter.compareToIgnoreCase("All Selected Chapters") == 0) {
            generateQuestions(chapters);
            generateQuestionsContainer();
            frame.remove(questionsContainer);
            frame.revalidate();
        } else {
            ArrayList<String> oneChapter = new ArrayList<String>();
            oneChapter.add(selectedChapter);
            generateQuestions(oneChapter);
            //frame.remove(questionsContainer);
            //questionsContainer = generateQuestionsContainer();            
            generateQuestionsContainer();
            frame.revalidate();
            frame.repaint();
        }
    }

    private JPanel generateQuestionsContainer() {
        //first clear previous buttons....
        buttonList.clear();
        //creating the container where questions will be printed...
        questionsContainer.removeAll();
        questionsContainer.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        questionsContainer.setSize(new Dimension(900, 1000));
        questionsContainer.setBackground(Color.white);
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        //vertical space at the above first heading
        questionsContainer.add(Box.createRigidArea(new Dimension(5, 19)));

        for (String question : questionsList) {
            JPanel questionRow = new JPanel();
            questionRow.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            questionRow.setBackground(Color.white);
            questionRow.setLayout(new BoxLayout(questionRow, BoxLayout.X_AXIS));
            //horizontal space before the button
            questionRow.add(Box.createRigidArea(new Dimension(8, 0)));
            JButton selectButton = new JButton("Replace");
            selectButton.addActionListener(this);
            buttonList.add(selectButton);
            questionRow.add(selectButton);

            //horizontal space after the button
            questionRow.add(Box.createRigidArea(new Dimension(8, 0)));

            JPanel questionPanel = parent.printQuestionWithImage(question, parent.parent.classx, parent.parent.subject);
            //questionPanel.setMaximumSize(questionPanel.getPreferredSize());            
            questionRow.add(questionPanel);
            questionRow.setMaximumSize(questionRow.getPreferredSize());
            questionsContainer.add(questionRow);

            //vertical space after each row
            questionsContainer.add(Box.createRigidArea(new Dimension(5, 19)));
        }
        return questionsContainer;
    }

}
