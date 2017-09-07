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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import papersystem.CustomPatternPanel;
import papersystem.MainScreen;

/**
 *
 * @author Abbasi
 */
public class CustomQuestionSelector implements ActionListener {

    //Select Buttons are added to this list..
    ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
    //vars to hold info about shownn questions
    ArrayList<String> questionsList = new ArrayList<>();
    ArrayList<Integer> qIDList = new ArrayList<>();
    //data for finding the questions to show..
    ArrayList<String> chapters = null;
    //holds ID of qType to show...
    int qTypeID;
    //holds number of question to select
    int toAsk;
    //holds parent's calling row number so editing parent questions 
    //data becomes easier...
    int qTypeRowNo;
    //holds Name of qType to show...
    //Number of selected Questions...
    int selectedQs = 0;
    String qTypeName;
    //calling editor instance, for using listener..
    PaperEditor parendt;
    //selected question info displayed on top of frame;
    JLabel select;
    //frame in which every thing will be placed...
    JFrame frame;
    JPanel questionsContainer = new JPanel();

    //holds reference to parent for updating purposes
    CustomPatternPanel parent;
    String classx = null;
    String subject = null;

    public static void createSelector(int qTypeID, int toAsk, int qTypeRowNo,
            ArrayList<String> chapters, CustomPatternPanel parent) {
        CustomQuestionSelector selector = new CustomQuestionSelector();
        selector.chapters = chapters;
        selector.qTypeID = qTypeID;
        selector.toAsk = toAsk;
        selector.parent = parent;
        selector.qTypeRowNo = qTypeRowNo;

        ArrayList<String> oneQType = new ArrayList<>();
        oneQType.add("" + qTypeID);
        selector.qTypeName = DBManager.getInstance().getQuestionTypeNamesByIDs(oneQType).get(0);

        MainScreen.mainFrame.setEnabled(false);

        selector.classx = DBManager.getInstance().getClassByQTypeID(qTypeID);
        selector.subject = DBManager.getInstance().getSubjectByQTypeID(qTypeID);

//      No Need to generate question here... 
//      drop down menu change in gui calls the listener and questions are automatically generated...
        //call empty gui creator...        
        selector.printGUI();

        //generate questions for all selected chapters
        selector.generateQuestions(selector.chapters);
        //update GUI to add questions
        selector.generateQuestionsContainer();
        //selector.printGUI();
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
//        JLabel select = new JLabel("<html><b>Select Chapter: </b><html>");
        select = new JLabel(selectedQs + "/" + toAsk + " Selected");

        //JComboBox chapterSelector = new JComboBox();
        JPanel buttonsHolder = new JPanel();
        JButton button = new JButton("Done");
        button.addActionListener(this);
        buttonsHolder.add(button);
        button = new JButton("Cancel");
        button.addActionListener(this);
        buttonsHolder.add(button);
        button = new JButton("Select All");
        button.addActionListener(this);
        buttonsHolder.add(button);
        button = new JButton("Deselect All");
        button.addActionListener(this);
        buttonsHolder.add(button);

        //topPanel.add(heading);
        topPanel.add(select);
        topPanel.add(buttonsHolder);
        //topPanel.add(chapterSelector);
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
        frame.revalidate();
        frame.repaint();

//        chapterSelector.addItem("All Selected Chapters");
//        for (String c : chapters) {
//            chapterSelector.addItem(c);
//        }
//        chapterSelector.addItemListener(new ItemListener() {
//
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    chapterChanged(chapterSelector.getSelectedItem().toString());
//                }
//            }
//        });
//        if (chapters.size() > 0) {
//            chapterSelector.setSelectedIndex(1);
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton) {
            JButton button = (JButton) source;
            if (button.getText().equalsIgnoreCase("DONE")) {
                if (selectedQs != toAsk) {
                    JOptionPane.showMessageDialog(frame, "Please select " + toAsk + " Questions");
                    return;
                }
                ArrayList<Integer> checkedIDs = new ArrayList<>();
                for (int i = 0; i < qIDList.size(); i++) {
                    if(checkBoxList.get(i).isSelected()){
                        checkedIDs.add(qIDList.get(i));
                    }
                }
                parent.updateQuestionsList(checkedIDs, qTypeRowNo);
                frame.dispose();
                onClose();
                return;
            } else if (button.getText().equalsIgnoreCase("Cancel")) {
                frame.dispose();
                onClose();
                return;
            } else if (button.getText().equalsIgnoreCase("Select All")) {
                for (JCheckBox c : checkBoxList) {
                    c.setSelected(true);
                }
                selectedQs = checkBoxList.size();
                select.setText(selectedQs + "/" + toAsk + " Selected");
                return;
            } else if (button.getText().equalsIgnoreCase("Deselect All")) {
                for (JCheckBox c : checkBoxList) {
                    c.setSelected(false);
                }
                selectedQs = 0;
                select.setText(selectedQs + "/" + toAsk + " Selected");
                return;
            }
            System.out.println("jbutton clicked");
            return;
        } else if (source instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) source;
            if (checkBox.isSelected()) {
                selectedQs++;
            } else {
                selectedQs--;
            }
            select.setText(selectedQs + "/" + toAsk + " Selected");
            System.out.println("jcheckbox clicked");
            return;
        }
    }

    private void generateQuestions(ArrayList<String> chapters) {
        //first clear all previous questions...
        questionsList.clear();
        qIDList.clear();
        //find questions from database...

        qIDList.addAll(DBManager.getInstance().getAllNotAddedQuestionsIDs(
                qTypeName,
                DBManager.getInstance().findSubjectID(subject, classx, null),
                chapters, new ArrayList<String>(), classx));
        questionsList = DBManager.getInstance().getQuestionsByIDs(qIDList, classx, subject);

        System.out.println(questionsList.size() + " ----- " + qIDList.size());

        for (int a = 0; a < qIDList.size(); a++) {
            int compare = questionsList.get(a).compareTo(DBManager.getInstance().getQuestion(qIDList.get(a), classx, subject));
            if (compare != 0) {
                System.out.println("ERROR:::::: ID MISMATCH!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
        System.out.println("HUIHUIHUI   " + qIDList.size() + " Questiosn available");
    }

    private void onClose() {
        MainScreen.mainFrame.setAlwaysOnTop(true);
        MainScreen.mainFrame.setAlwaysOnTop(false);
        MainScreen.mainFrame.setEnabled(true);
    }

    private void chapterChanged(String selectedChapter) {
//        System.out.println("New selected chapter is " + selectedChapter);
//        //check if not the first time...
//        if (questionsContainer == null) {
//            return;
//        }
//        //frame.remove(questionsContainer);
//        questionsList.clear();
//        generateQuestionsContainer();
//
//        if (selectedChapter.compareToIgnoreCase("All Selected Chapters") == 0) {
//            generateQuestions(chapters);
//            generateQuestionsContainer();
//            //frame.remove(questionsContainer);
//            frame.revalidate();
//            frame.repaint();
//        } else {
//            ArrayList<String> oneChapter = new ArrayList<String>();
//            oneChapter.add(selectedChapter);
//            generateQuestions(oneChapter);
//            //frame.remove(questionsContainer);
//            //questionsContainer = generateQuestionsContainer();            
//            generateQuestionsContainer();
//            frame.revalidate();
//            frame.repaint();
//        }
    }

    private JPanel generateQuestionsContainer() {
        //first clear previous buttons....
        checkBoxList.clear();
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
            //horizontal space before the checkbox
            questionRow.add(Box.createRigidArea(new Dimension(8, 0)));

            JCheckBox check = new JCheckBox("");
            check.setBackground(Color.white);
            check.addActionListener(this);
            questionRow.add(check);
            checkBoxList.add(check);
//            JButton selectButton = new JButton("Select");
//            selectButton.addActionListener(this);
//            checkBoxList.add(selectButton);
//            questionRow.add(selectButton);

//            horizontal space after the button
            questionRow.add(Box.createRigidArea(new Dimension(8, 0)));

            JPanel questionPanel = PaperEditor.printQuestionWithImage(question, classx, subject);
            //questionPanel.setMaximumSize(questionPanel.getPreferredSize());            
            questionRow.add(questionPanel);
            questionRow.setMaximumSize(questionRow.getPreferredSize());
            questionsContainer.add(questionRow);

            //vertical space after each row
            questionsContainer.add(Box.createRigidArea(new Dimension(5, 19)));
        }

//        if (questionsList.isEmpty()) {
//            questionsContainer.setBackground(Color.white);
//
////            questionsContainer.add(new JLabel("<html><b>Loading Questions... Please Wait</b></html>"));
//            questionsContainer.add(new JLabel("Loading Questions... Please Wait"));
//            System.out.println("Loading Questions... Please Wait");
//        }
        return questionsContainer;
    }

}
