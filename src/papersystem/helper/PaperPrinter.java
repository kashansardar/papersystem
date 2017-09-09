package papersystem.helper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import papersystem.MainScreen;

/**
 *
 * @author Abbasi
 * @author Kashan
 */
class FourFields {

    String f1, f2, f3, f4 = "";
    int questionID = -1;//will contain -1 if no question found
    boolean heading = false;
    boolean question = false;

    public FourFields(String f1, String f2, String f3, String f4, boolean heading, boolean question) {
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;

        this.heading = heading;
        this.question = question;
    }

    public FourFields(String f1, String f2, String f3, String f4, int qID, boolean heading, boolean question) {
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;

        this.questionID = qID;
        this.heading = heading;
        this.question = question;
    }
}

public class PaperPrinter {

    static boolean alreadyRunning = false;
    //this int is used by generate Questions Method... 
    //it increments the number as each question is added to printList...
    int questionNumber = 1;
    //This is the question number printed on left...
    int questionNumberPrinted = 1;
    String paperName, date, Instructions, classx, subject, board = "";
    String patternToAsk, patternToAns, patternMarks = "";
    int subjectID = -1;
    //holds the parent frame..
    MainScreen parent = null;
    //contains the chapters that are included in paper..
    ArrayList<String> chapters = null;
    //this list holds the question ids of Questions already added in printList...
    static ArrayList<String> addedQuestionIDs = new ArrayList<>();
    //Generate Question adds to this list.. it is then edited by user and printed ..
    static ArrayList<FourFields> printList = new ArrayList<>();
    //Generate Answer adds to this list.. it is then edited by user and printed ..
    static ArrayList<FourFields> answerList = new ArrayList<>();
    //this variable acts as index to navigate addedQuestionIDs in generateAnswerPaper...
    int indexForGenerateAns = 0;
    String totalMarks = "0";
    private String totalTime = "3 Hrs";

    JDialog loading;
    //this holds lists of all question ids... in each list sorted by qtype recieved...
    private ArrayList<ArrayList<Integer>> listOfQuestionTypes_Custom;
    
    PaperPrinter current = null; // this piece of code is very important
    String patternTypes = null;
    
    //public method for printing... display edit menu..
    public void print(ArrayList<String> chapters, String paperName, String date, String time,
            String Instructions, String classx, String subject, int patternID, String board, MainScreen parent) {
        
        parent.setEnabled(false);
        //setupLoader(parent);
        this.chapters = chapters;
        this.paperName = paperName;
        this.date = date;
        this.Instructions = Instructions;
        this.classx = classx;
        this.subject = subject;
        this.parent = parent;
        this.board = board;
        this.subjectID = DBManager.getInstance().findSubjectID(subject, classx, board);
        this.totalTime = time;
        this.current = this; //set the instance that invokes this method to the current instance of the class
        this.patternTypes = DBManager.getInstance().getPatternTypes(patternID);
        
        // clear previous data in the lists since the lists are static for all instances.
        printList.clear();
        addedQuestionIDs.clear();
        answerList.clear();
        //addedQuestionIDs = new ArrayList<>();
        //printList = new ArrayList<>();
        //answerList = new ArrayList<>();
        
        //TODO Get paper pattern 
        DBManager dm = DBManager.getInstance();
        String patternTypes = dm.getPatternTypes(patternID);
        patternToAsk = dm.getPatternToAsk(patternID);
        patternToAns = dm.getPatternToAns(patternID);
        patternMarks = dm.getPatternMarks(patternID);
        totalMarks = dm.getTotalMarks(patternID);
        generateQuesions(patternTypes);
        questionNumber = 1;
        questionNumberPrinted = 1;
        indexForGenerateAns = 0;
        generateAnswers(patternTypes);
        printLogPrintList();
        PaperEditor.generateEditMenu(this, printList, answerList);
    }
    
    //public method for printing Custom Pattern... display edit menu..
    public void print_Custom(ArrayList<String> chapters, String paperName, String date, String time,
            String Instructions, String classx, String subject, int patternID, String board, MainScreen parent,
            ArrayList<ArrayList<Integer>> listOfQuestionTypes) {

        parent.setEnabled(false);
        //setupLoader(parent);
        this.chapters = chapters;
        this.paperName = paperName;
        this.date = date;
        this.Instructions = Instructions;
        this.classx = classx;
        this.subject = subject;
        this.parent = parent;
        this.board = board;
        this.subjectID = DBManager.getInstance().findSubjectID(subject, classx, board);
        this.totalTime = time;
        this.listOfQuestionTypes_Custom = listOfQuestionTypes;
        this.current = this; //set the instance that invokes this method to the current instance of the class
        this.patternTypes = DBManager.getInstance().getPatternTypes(patternID);
        // clear previous data in the lists since the lists are static for all instances.
        printList.clear();
        addedQuestionIDs.clear();
        answerList.clear();
        //addedQuestionIDs = new ArrayList<>();
        //printList = new ArrayList<>();
        //answerList = new ArrayList<>();

        //TODO Get paper pattern 
        DBManager dm = DBManager.getInstance();
        String patternTypes = dm.getPatternTypes(patternID);
        patternToAsk = dm.getPatternToAsk(patternID);
        patternToAns = dm.getPatternToAns(patternID);
        patternMarks = dm.getPatternMarks(patternID);
        totalMarks = dm.getTotalMarks(patternID);
        generateQuesions_Custom(patternTypes);
        questionNumber = 1;
        questionNumberPrinted = 1;
        indexForGenerateAns = 0;
        generateAnswers_Custom(patternTypes);
        printLogPrintList();
        PaperEditor.generateEditMenu(this, printList, answerList);
    }

    private void generateQuesions(String patternTypes) {
        //If questions need to ORed...

        if (patternTypes.startsWith("[|")) {
            String[] s = patternTypes.split("\n");

            //skip first value i.e. "[|"
            for (int i = 1; i < s.length; i++) {
                String token = s[i].trim();
                System.out.println("Token::: " + token);
                //this case only triggers if first line of pattern is [& or [|
                if (token.compareToIgnoreCase("]") == 0) {
                    String remaining = "";
                    for (int j = i + 1; j < s.length; j++) {
                        remaining = remaining + "\n" + s[j];
                    }
                    //deleting the last OR
                    if (printList.get(printList.size() - 1).f3.equals("OR")) {
                        printList.remove(printList.size() - 1);
                    }
                    questionNumberPrinted++;

                    if (remaining.isEmpty()) {
                        return;
                    }

                    //to remove first \n
                    remaining = remaining.substring(1);
                    generateQuesions(remaining);
                    break;
                }

                //add Question number, type, number of questions to answer, marks..
                //prepare question type...
                String f1 = "";
                if (i == 1) {//only print first time...
                    f1 = "Q." + questionNumberPrinted;
                }
                String f2 = IntegerToAlphabet(i);
                String f3 = token + formatStringToAns(questionNumber);
                String f4 = "" + Integer.parseInt(getMarks(questionNumber)) * getToAns(questionNumber);

                //add to list...
                printList.add(new FourFields(f1, f2, f3, f4, false, false));

                //loop and get all the questions and add into 4fields..
                for (int j = 0; j < getToAsk(questionNumber); j++) {
                    //prepare question...
                    //int marks = Integer.parseInt(getMarks(questionNumber));
                    // int qID = DBManager.getInstance().getRandomQuestion2(token, marks, DBManager.getInstance().findSubjectID(subject, classx, board),chapters, addedQuestionIDs);
                    int qID = DBManager.getInstance().getRandomQuestion(token, DBManager.getInstance().findSubjectID(subject, classx, board), chapters, addedQuestionIDs, classx);
                    if (qID != -1) {
                        addedQuestionIDs.add("" + qID);
                    }
                    f1 = "";
                    f2 = "";
                    if (getToAsk(questionNumber) > 1) {//if more than one questions
                        f2 = IntegerToRomanNumeral(j + 1);
                    }
                    f3 = "";
                    if (qID != -1) {
                        f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                    }
                    f4 = "";

                    //add to list...
                    printList.add(new FourFields(f1, f2, f3, f4, qID, false, true));
                }
                //Adding OR heading
                printList.add(new FourFields("", "", "OR", "", true, false));
                questionNumber++;
            }
            //deleting the last OR
            if (printList.get(printList.size() - 1).f3.equals("OR")) {
                printList.remove(printList.size() - 1);
            }
            questionNumberPrinted++;
        } //If questions need to ANDed...
        else if (patternTypes.startsWith("[&")) {
            String[] s = patternTypes.split("\n");

            //skip first value i.e. "[|"
            for (int i = 1; i < s.length; i++) {
                String token = s[i].trim();
                System.out.println("Token::: " + token);
                //this case only triggers if first line of pattern is [& or [|
                if (token.compareToIgnoreCase("]") == 0) {
                    String remaining = "";
                    for (int j = i + 1; j < s.length; j++) {
                        remaining = remaining + "\n" + s[j];
                    }
                    if (remaining.isEmpty()) {
                        return;
                    }
                    //to remove first \n
                    remaining = remaining.substring(1);
                    questionNumberPrinted++;
                    generateQuesions(remaining);
                    break;
                }

                //add Question number, type, number of questions to answer, marks..
                //prepare question type...
                String f1 = "";
                if (i == 1) {//only print first time...
                    f1 = "Q." + questionNumberPrinted;
                }
                String f2 = IntegerToAlphabet(i);
                String f3 = token + formatStringToAns(questionNumber);
                String f4 = "" + Integer.parseInt(getMarks(questionNumber)) * getToAns(questionNumber);

                //add to list...
                printList.add(new FourFields(f1, f2, f3, f4, false, false));

                //loop and get all the questions and add into 4fields..
                for (int j = 0; j < getToAsk(questionNumber); j++) {
                    //prepare question...
                    //int marks = Integer.parseInt(getMarks(questionNumber));
                    // int qID = DBManager.getInstance().getRandomQuestion2(token, marks, DBManager.getInstance().findSubjectID(subject, classx, board),chapters, addedQuestionIDs);
                    int qID = DBManager.getInstance().getRandomQuestion(token, DBManager.getInstance().findSubjectID(subject, classx, board), chapters, addedQuestionIDs, classx);
                    if (qID != -1) {
                        addedQuestionIDs.add("" + qID);
                    }
                    f1 = "";
                    f2 = "";
                    if (getToAsk(questionNumber) > 1) {//if more than one questions
                        f2 = IntegerToRomanNumeral(j + 1);
                    }
                    f3 = "";
                    if (qID != -1) {
                        f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                    }
                    f4 = "";

                    //add to list...
                    printList.add(new FourFields(f1, f2, f3, f4, qID, false, true));
                }
                questionNumber++;
            }
            questionNumberPrinted++;
        } //If normal sequence
        else {
            String[] s = patternTypes.split("\n");

            for (int i = 0; i < s.length; i++) {
                String token = s[i].trim();
                System.out.println("Token::: " + token);

                //if heading add in 4fields..
                if (token.compareToIgnoreCase("{{") == 0) {
                    String heading = "";
                    //move to first line of heading...
                    token = s[++i];
                    while (token.compareToIgnoreCase("}}") != 0) {
                        heading = heading + "\n" + token;
                        token = s[++i];
                    }
                    //remove first extra \n character...
                    heading = heading.substring(1);
                    printList.add(new FourFields("", "", heading, "", true, false));
                } //else if [| or [&, loop and get sub-questions, call generate Questions again..
                else if (token.compareToIgnoreCase("[|") == 0
                        || token.compareToIgnoreCase("[&") == 0) {
                    //move to first line of heading...
                    String sub = "";
                    while (token.compareToIgnoreCase("]") != 0) {
                        sub = sub + "\n" + token;
                        token = s[++i];
                    }
                    //remove first extra \n character...
                    sub = sub.substring(1);
                    //sub questions list is sent recursively.. note that last ] is not sent...
                    generateQuesions(sub);
                } //else if question type, add Question number, type, number of questions to answer, marks..
                else {
                    //prepare question type...
                    String f1 = "Q." + questionNumberPrinted;
                    String f2 = "";
                    //TODO
                    String f3 = ASDF(token) + formatStringToAns(questionNumber);
                    String f4 = "" + Integer.parseInt(getMarks(questionNumber)) * getToAns(questionNumber);

                    //add to list...
                    printList.add(new FourFields(f1, f2, f3, f4, false, false));

                    //loop and get all the questions and add into 4fields..
                    for (int j = 0; j < getToAsk(questionNumber); j++) {
                        //prepare question...
                        //int marks = Integer.parseInt(getMarks(questionNumber));
                        // int qID = DBManager.getInstance().getRandomQuestion2(token, marks, DBManager.getInstance().findSubjectID(subject, classx, board),chapters, addedQuestionIDs);
                        int qID = DBManager.getInstance().getRandomQuestion(token, DBManager.getInstance().findSubjectID(subject, classx, board), chapters, addedQuestionIDs, classx);
                        if (qID != -1) {
                            addedQuestionIDs.add("" + qID);
                        }
                        f1 = "";
                        f2 = "";
                        if (getToAsk(questionNumber) > 1) {//if more than one questions
                            //TODO... decide Alphabet or roman count
                            f2 = IntegerToRomanNumeral(j + 1);
                        }
                        f3 = "";
                        if (qID != -1) {
                            f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                        }
                        f4 = "";//getMarks(questionNumber);

                        //add to list...
                        printList.add(new FourFields(f1, f2, f3, f4, qID, false, true));
                    }
                    questionNumber++;
                    questionNumberPrinted++;
                }
            }
        }
    }

    private void generateQuesions_Custom(String patternTypes) {
        String[] s = patternTypes.split("\n");

        for (int i = 0; i < s.length; i++) {
            String token = s[i].trim();
            System.out.println("Token::: " + token);

            //else if question type, add Question number, type, number of questions to answer, marks..
            //prepare question type...
            String f1 = "Q." + questionNumberPrinted;
            String f2 = "";
            String f3 = ASDF(token) + formatStringToAns(questionNumber);
            String f4 = "" + Integer.parseInt(getMarks(questionNumber)) * getToAns(questionNumber);

            //add to list...
            printList.add(new FourFields(f1, f2, f3, f4, false, false));

            //loop and get all the questions and add into 4fields..
            for (int j = 0; j < getToAsk(questionNumber); j++) {
                //prepare question...
                int qID = listOfQuestionTypes_Custom.get(i).get(j);
                if (qID != -1) {
                    addedQuestionIDs.add("" + qID);
                }
                f1 = "";
                f2 = "";
                if (getToAsk(questionNumber) > 1) {//if more than one questions
                    //TODO... decide Alphabet or roman count
                    f2 = IntegerToRomanNumeral(j + 1);
                }
                f3 = "";
                if (qID != -1) {
                    f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                }
                f4 = "";//getMarks(questionNumber);

                //add to list...
                printList.add(new FourFields(f1, f2, f3, f4, qID, false, true));
            }
            questionNumber++;
            questionNumberPrinted++;
        }
    }

    private void generateAnswers(String patternTypes) {

        //If questions need to ORed...
        if (patternTypes.startsWith("[|")) {
            String[] s = patternTypes.split("\n");

            //skip first value i.e. "[|"
            for (int i = 1; i < s.length; i++) {
                String token = s[i].trim();
                System.out.println("Token::: " + token);
                //this case only triggers if first line of pattern is [& or [|
                if (token.compareToIgnoreCase("]") == 0) {
                    String remaining = "";
                    for (int j = i + 1; j < s.length; j++) {
                        remaining = remaining + "\n" + s[j];
                    }
                    if (remaining.isEmpty()) {
                        return;
                    }
                    //to remove first \n
                    remaining = remaining.substring(1);
                    questionNumberPrinted++;
                    generateAnswers(remaining);
                    break;
                }

                //add Question number, type, number of questions to answer, marks..
                //prepare question type...
                String f1 = "";
                if (i == 1) {//only print first time...
                    f1 = "Q." + questionNumberPrinted;
                }
                String f2 = IntegerToAlphabet(i);
                String f3 = token + formatStringToAns(questionNumber);
                String f4 = getMarks(questionNumber);

                //add to list...
                answerList.add(new FourFields(f1, f2, f3, f4, false, false));

                //loop and get all the questions and add into 4fields..
                for (int j = 0; j < getToAsk(questionNumber); j++) {
                    //prepare question...
                    //int marks = Integer.parseInt(getMarks(questionNumber));
                    // int qID = DBManager.getInstance().getRandomQuestion2(token, marks, DBManager.getInstance().findSubjectID(subject, classx, board),chapters, addedQuestionIDs);
                    int qID = -1;
                    if (addedQuestionIDs.size() > 0) {
                        if (addedQuestionIDs.size() > indexForGenerateAns) {
                            qID = Integer.parseInt(addedQuestionIDs.get(indexForGenerateAns++));
                        }
                    }
                    f1 = "";
                    f2 = "";
                    if (getToAsk(questionNumber) > 1) {//if more than one questions
                        f2 = IntegerToRomanNumeral(j + 1);
                    }
                    f3 = "";
                    if (qID != -1) {
                        f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                    }
                    f4 = "";

                    //add to list...
                    answerList.add(new FourFields(f1, f2, f3, f4, qID, false, true));

                    if (qID != -1) {
                        f3 = DBManager.getInstance().getAnswer(qID, classx, subject);
                    }
                    answerList.add(new FourFields(f1, "Ans.", f3, f4, qID, false, false));

                }
                //Adding OR heading
                answerList.add(new FourFields("", "", "OR", "", true, false));
                questionNumber++;
            }
            //deleting the last OR
            answerList.remove(answerList.size() - 1);
            questionNumberPrinted++;
        } //If questions need to ANDed...
        else if (patternTypes.startsWith("[&")) {
            String[] s = patternTypes.split("\n");

            //skip first value i.e. "[|"
            for (int i = 1; i < s.length; i++) {
                String token = s[i].trim();
                System.out.println("Token::: " + token);
                //this case only triggers if first line of pattern is [& or [|
                if (token.compareToIgnoreCase("]") == 0) {
                    String remaining = "";
                    for (int j = i + 1; j < s.length; j++) {
                        remaining = remaining + "\n" + s[j];
                    }
                    if (remaining.isEmpty()) {
                        return;
                    }
                    //to remove first \n
                    remaining = remaining.substring(1);
                    questionNumberPrinted++;
                    generateAnswers(remaining);
                    break;
                }

                //add Question number, type, number of questions to answer, marks..
                //prepare question type...
                String f1 = "";
                if (i == 1) {//only print first time...
                    f1 = "Q." + questionNumberPrinted;
                }
                String f2 = IntegerToAlphabet(i);
                String f3 = token + formatStringToAns(questionNumber);
                String f4 = "" + Integer.parseInt(getMarks(questionNumber)) * getToAns(questionNumber);

                //add to list...
                answerList.add(new FourFields(f1, f2, f3, f4, false, false));

                //loop and get all the questions and add into 4fields..
                for (int j = 0; j < getToAsk(questionNumber); j++) {
                    //prepare question...
                    //int marks = Integer.parseInt(getMarks(questionNumber));
                    // int qID = DBManager.getInstance().getRandomQuestion2(token, marks, DBManager.getInstance().findSubjectID(subject, classx, board),chapters, addedQuestionIDs);
                    int qID = -1;
                    if (addedQuestionIDs.size() > 0) {
                        if (addedQuestionIDs.size() > indexForGenerateAns) {
                            qID = Integer.parseInt(addedQuestionIDs.get(indexForGenerateAns++));
                        }
                    }
                    if (qID != -1) {
//                        addedQuestionIDs.add("" + qID);
                    }
                    f1 = "";
                    f2 = "";
                    if (getToAsk(questionNumber) > 1) {//if more than one questions
                        f2 = IntegerToRomanNumeral(j + 1);
                    }
                    f3 = "";
                    if (qID != -1) {
                        f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                    }
                    f4 = "";

                    //add to list...
                    answerList.add(new FourFields(f1, f2, f3, f4, qID, false, true));

                    if (qID != -1) {
                        f3 = DBManager.getInstance().getAnswer(qID, classx, subject);
                    }
                    answerList.add(new FourFields(f1, "Ans.", f3, f4, qID, false, false));
                }
                questionNumber++;
            }
            questionNumberPrinted++;
        } //If normal sequence
        else {
            String[] s = patternTypes.split("\n");

            for (int i = 0; i < s.length; i++) {
                String token = s[i].trim();
                System.out.println("Token::: " + token);

                //if heading add in 4fields..
                if (token.compareToIgnoreCase("{{") == 0) {
                    String heading = "";
                    //move to first line of heading...
                    token = s[++i];
                    while (token.compareToIgnoreCase("}}") != 0) {
                        heading = heading + "\n" + token;
                        token = s[++i];
                    }
                    //remove first extra \n character...
                    heading = heading.substring(1);
                    answerList.add(new FourFields("", "", heading, "", true, false));
                } //else if [| or [&, loop and get sub-questions, call generate Questions again..
                else if (token.compareToIgnoreCase("[|") == 0
                        || token.compareToIgnoreCase("[&") == 0) {
                    //move to first line of heading...
                    String sub = "";
                    while (token.compareToIgnoreCase("]") != 0) {
                        sub = sub + "\n" + token;
                        token = s[++i];
                    }
                    //remove first extra \n character...
                    sub = sub.substring(1);
                    //sub questions list is sent recursively.. note that last ] is not sent...
                    generateAnswers(sub);
                } //else if question type, add Question number, type, number of questions to answer, marks..
                else {
                    //prepare question type...
                    String f1 = "Q." + questionNumberPrinted;
                    String f2 = "";
                    //TODO
                    String f3 = ASDF(token) + formatStringToAns(questionNumber);
                    String f4 = "" + Integer.parseInt(getMarks(questionNumber)) * getToAns(questionNumber);

                    //add to list...
                    answerList.add(new FourFields(f1, f2, f3, f4, false, false));

                    //loop and get all the questions and add into 4fields..
                    for (int j = 0; j < getToAsk(questionNumber); j++) {
                        //prepare question...
                        //int marks = Integer.parseInt(getMarks(questionNumber));
                        // int qID = DBManager.getInstance().getRandomQuestion2(token, marks, DBManager.getInstance().findSubjectID(subject, classx, board),chapters, addedQuestionIDs);
                        int qID = -1;
                        if (addedQuestionIDs.size() > 0) {
                            if (addedQuestionIDs.size() > indexForGenerateAns) {
                                qID = Integer.parseInt(addedQuestionIDs.get(indexForGenerateAns++));
                            }
                        }
                        if (qID != -1) {
//                            addedQuestionIDs.add("" + qID);
                        }
                        f1 = "";
                        f2 = "";
                        if (getToAsk(questionNumber) > 1) {//if more than one questions
                            //TODO... decide Alphabet or roman count
                            f2 = IntegerToRomanNumeral(j + 1);
                        }
                        f3 = "";
                        if (qID != -1) {
                            f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                        }
                        f4 = "";//getMarks(questionNumber);

                        //add to list...
                        answerList.add(new FourFields(f1, f2, f3, f4, qID, false, true));

                        if (qID != -1) {
                            f3 = DBManager.getInstance().getAnswer(qID, classx, subject);
                        }
                        answerList.add(new FourFields(f1, "Ans.", f3, f4, qID, false, false));
                    }
                    questionNumber++;
                    questionNumberPrinted++;
                }
            }
        }
    }

    private void generateAnswers_Custom(String patternTypes) {
        String[] s = patternTypes.split("\n");

        for (int i = 0; i < s.length; i++) {
            String token = s[i].trim();
            System.out.println("Token::: " + token);

            //else if question type, add Question number, type, number of questions to answer, marks..
            //prepare question type...
            String f1 = "Q." + questionNumberPrinted;
            String f2 = "";
            String f3 = ASDF(token) + formatStringToAns(questionNumber);
            String f4 = "" + Integer.parseInt(getMarks(questionNumber)) * getToAns(questionNumber);

            //add to list...
            answerList.add(new FourFields(f1, f2, f3, f4, false, false));

            //loop and get all the questions and add into 4fields..
            for (int j = 0; j < getToAsk(questionNumber); j++) {
                //prepare question...
                int qID = -1;
                if (addedQuestionIDs.size() > 0) {
                    if (addedQuestionIDs.size() > indexForGenerateAns) {
                        qID = Integer.parseInt(addedQuestionIDs.get(indexForGenerateAns++));
                    }
                }
                f1 = "";
                f2 = "";
                if (getToAsk(questionNumber) > 1) {//if more than one questions
                    //TODO... decide Alphabet or roman count
                    f2 = IntegerToRomanNumeral(j + 1);
                }
                f3 = "";
                if (qID != -1) {
                    f3 = DBManager.getInstance().getQuestion(qID, classx, subject);
                }
                f4 = "";//getMarks(questionNumber);

                //add to list...
                answerList.add(new FourFields(f1, f2, f3, f4, qID, false, true));

                if (qID != -1) {
                    f3 = DBManager.getInstance().getAnswer(qID, classx, subject);
                }
                answerList.add(new FourFields(f1, "Ans.", f3, f4, qID, false, false));
            }
            questionNumber++;
            questionNumberPrinted++;
        }
    }

    //Get the marks for question number given
    private String getMarks(int questionNumber) {
        return patternMarks.split(";")[questionNumber - 1];
    }

    private String formatStringToAns(int questionNumber) {
        //if paper has no choice
        if (patternToAns.compareToIgnoreCase("all") == 0) {
            return "";
        } //if some questions of the paper have some choice...
        else {
            int toAsk = Integer.parseInt(patternToAsk.split(";")[questionNumber - 1]);
            int toAns = Integer.parseInt(patternToAns.split(";")[questionNumber - 1]);

            //if no choice...
            if (toAsk == toAns) {
                return "";
            } else {
                return " (ANY " + toAns + ")";
            }
        }
    }

    private int getToAsk(int questionNumber) {
        return Integer.parseInt(patternToAsk.split(";")[questionNumber - 1]);
    }

    private int getToAns(int questionNumber) {
        if (patternToAns.compareTo("all") == 0) {
            return getToAsk(questionNumber);
        }
        return Integer.parseInt(patternToAns.split(";")[questionNumber - 1]);
    }

    private static String IntegerToRomanNumeral(int input) {
        if (input < 1 || input > 3999) {
            return "Invalid Roman Number Value";
        }
        String s = "";
        while (input >= 1000) {
            s += "m";
            input -= 1000;
        }
        while (input >= 900) {
            s += "c";
            input -= 900;
        }
        while (input >= 500) {
            s += "d";
            input -= 500;
        }
        while (input >= 400) {
            s += "cd";
            input -= 400;
        }
        while (input >= 100) {
            s += "c";
            input -= 100;
        }
        while (input >= 90) {
            s += "xc";
            input -= 90;
        }
        while (input >= 50) {
            s += "l";
            input -= 50;
        }
        while (input >= 40) {
            s += "xl";
            input -= 40;
        }
        while (input >= 10) {
            s += "x";
            input -= 10;
        }
        while (input >= 9) {
            s += "ix";
            input -= 9;
        }
        while (input >= 5) {
            s += "v";
            input -= 5;
        }
        while (input >= 4) {
            s += "iv";
            input -= 4;
        }
        while (input >= 1) {
            s += "i";
            input -= 1;
        }
        return "(" + s + ")";
    }

    private String IntegerToAlphabet(int i) {
        int base = 'A';

        if (i <= 26) {
            return "(" + ((char) (base - 1 + i)) + ")";
        } else if (i < 702) {
            return "(" + ((char) (base - 1 + i / 26)) + ((char) (base + i % 26)) + ")";
        } else {
            return "( )";
        }
    }

    private void printLogPrintList() {
        for (FourFields ff : printList) {
            //System.out.println(ff.f1 + "\t" + ff.f2 + "\t" + ff.f3.split("\n")[0] + "\t" + ff.f4);
            System.out.println(ff.f1 + "\t" + ff.f2 + "\t" + ff.f3 + "\t" + ff.f4);
        }
    }

    public void printDocFile() {
        FileOutputStream out = null;
        try {
            //Blank Document
            XWPFDocument document = new XWPFDocument();

            //Write the Document in file system
            String fileName = getRandomQuestionPaperFileName("temp//");
            out = new FileOutputStream(new File(fileName));

            addHeaderInDoc(document, false);
            for (FourFields ff : printList) {
                addTableInDoc(document, ff.f1, ff.f2, ff.f3, ff.f4, ff.heading);
            }
            if (subject.equalsIgnoreCase("Marathi") || subject.equalsIgnoreCase("Hindi") || subject.equalsIgnoreCase("Marathi Writing Skill") || subject.equalsIgnoreCase("Hindi Writing Skill")) {
                document = changeFontOfDocument(document);
            }
            document.write(out);
            System.out.println(fileName + " written successully");
            out.close();
            Desktop.getDesktop().open(new File(fileName));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printAnsDocFile() {
        FileOutputStream out = null;
        try {
            //Blank Document
            XWPFDocument document = new XWPFDocument();

            //Write the Document in file system
            String fileName = getRandomQuestionPaperFileName("temp//");
            out = new FileOutputStream(new File(fileName));

            addHeaderInDoc(document, true);
            for (FourFields ff : answerList) {
                addTableInDoc(document, ff.f1, ff.f2, ff.f3, ff.f4, ff.heading);
            }
            if (subject.equalsIgnoreCase("Marathi") || subject.equalsIgnoreCase("Hindi") || subject.equalsIgnoreCase("Marathi Writing Skill") || subject.equalsIgnoreCase("Hindi Writing Skill")) {
                document = changeFontOfDocument(document);
            }
            document.write(out);
            System.out.println(fileName + " written successully");
            out.close();
            Desktop.getDesktop().open(new File(fileName));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void widthCellsAcrossRow(XWPFTable table, int rowNum, int colNum, int width) {
        XWPFTableCell cell = table.getRow(rowNum).getCell(colNum);
        if (cell.getCTTc().getTcPr() == null) {
            cell.getCTTc().addNewTcPr();
        }
        if (cell.getCTTc().getTcPr().getTcW() == null) {
            cell.getCTTc().getTcPr().addNewTcW();
        }
        cell.getCTTc().getTcPr().getTcW().setW(BigInteger.valueOf((long) width));
        cell.getCTTc().getTcPr().getTcW().setType(STTblWidth.DXA);
    }

    private void addHeaderInDoc(XWPFDocument document, boolean modelPaper) {
        //create table
        XWPFTable table;

        int ins = 0;
        if (Instructions != null) {
            ins = 1;
        }

//        table = document.createTable(3 + ins, 3);
        table = document.createTable(3, 3);

        //setting table size..
        CTTbl tablex = table.getCTTbl();
        CTTblPr pr = tablex.getTblPr();
        CTTblWidth tblW = pr.getTblW();
        tblW.setW(BigInteger.valueOf(6000));
        tblW.setType(STTblWidth.PCT);
        pr.setTblW(tblW);
        tablex.setTblPr(pr);

        //centering table..
        CTJc jc = pr.addNewJc();
        jc.setVal(STJc.CENTER);
        pr.setJc(jc);

        //setting table border
        table.getCTTbl().getTblPr().getTblBorders().setNil();
        table.getCTTbl().getTblPr().addNewTblBorders().addNewBottom().setVal(STBorder.DOUBLE);
        table.getCTTbl().getTblPr().addNewTblBorders().addNewTop().setVal(STBorder.DOUBLE);
        table.getCTTbl().getTblPr().addNewTblBorders().addNewLeft().setVal(STBorder.DOUBLE);
        table.getCTTbl().getTblPr().addNewTblBorders().addNewRight().setVal(STBorder.DOUBLE);

        table.getRow(0).getCell(1).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = table.getRow(0).getCell(1).getParagraphs().get(0).createRun();
        run.setText(papersystem.PaperSystem.softwareName);
        run.addBreak();
        run.setBold(true);

        run = table.getRow(0).getCell(1).getParagraphs().get(0).createRun();
        run.setText(subject);
        run.addBreak();
        run.setBold(true);

        run = table.getRow(0).getCell(1).getParagraphs().get(0).createRun();
        run.setText(paperName);
        run.addBreak();
        run.setBold(true);

        if (modelPaper) {
            run = table.getRow(0).getCell(1).getParagraphs().get(0).createRun();
            run.setText("MODEL ANSWER");
            run.addBreak();
            run.setBold(true);
        }

        run = table.getRow(0).getCell(1).getParagraphs().get(0).createRun();
        run.setText("Max Marks: " + totalMarks);
        run.setBold(true);

        table.getRow(2).getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.LEFT);
        run = table.getRow(2).getCell(0).getParagraphs().get(0).createRun();
        run.setText("Date: " + date);
        run.setBold(true);

        table.getRow(2).getCell(1).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
        run = table.getRow(2).getCell(1).getParagraphs().get(0).createRun();
        run.setText("Std: " + classx);
        run.setBold(true);

        table.getRow(2).getCell(2).getParagraphs().get(0).setAlignment(ParagraphAlignment.RIGHT);
        run = table.getRow(2).getCell(2).getParagraphs().get(0).createRun();
        run.setText("Time: " + totalTime);
        run.setBold(true);

        //Removing space after each paragraph in table
        table.getRow(0).getCell(0).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(0).getCell(1).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(0).getCell(2).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(1).getCell(0).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(1).getCell(1).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(1).getCell(2).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(2).getCell(0).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(2).getCell(1).getParagraphs().get(0).setSpacingAfter(0);
        table.getRow(2).getCell(2).getParagraphs().get(0).setSpacingAfter(0);

        if (ins == 1) {
//            table.getRow(3).getCell(0).getParagraphs().get(0).setSpacingAfter(0);
//            table.getRow(3).getCell(1).getParagraphs().get(0).setSpacingAfter(0);
//            table.getRow(3).getCell(2).getParagraphs().get(0).setSpacingAfter(0);
//
//            table.getRow(3).getCell(1).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
//            run = table.getRow(3).getCell(1).getParagraphs().get(0).createRun();
//            run.setText(Instructions);
//            run.setBold(true);
            XWPFParagraph para = document.createParagraph();
            para.setAlignment(ParagraphAlignment.CENTER);
            run = para.createRun();
            run.setText(Instructions);
            run.setBold(true);
        }
        //Setting table columns width            
        widthCellsAcrossRow(table, 0, 0, 10000);
        widthCellsAcrossRow(table, 0, 1, 30000);
        widthCellsAcrossRow(table, 0, 2, 10000);

        document.createParagraph();
        System.out.println("Header added in Document");
    }

    private void addTableInDoc(XWPFDocument document, String f1, String f2, String f3, String f4, boolean heading) {

        //to remove extra :::::
        f3 = f3.replace("::", ":");
        f3 = f3.replace("::", ":");
        f3 = f3.replace("::", ":");
        f3 = f3.replace("::", ":");
        f3 = f3.replace("::", ":");

        //create table
        XWPFTable table;
        //if heading add one extra row...
        if (heading && f3.compareTo("OR") != 0) {
            table = document.createTable(1, 4);

            widthCellsAcrossRow(table, 0, 0, 0);
            widthCellsAcrossRow(table, 0, 1, 0);
            widthCellsAcrossRow(table, 0, 2, 6000);
            widthCellsAcrossRow(table, 0, 3, 0);

            //setting table size..
            CTTbl tablex = table.getCTTbl();
            CTTblPr pr = tablex.getTblPr();
            CTTblWidth tblW = pr.getTblW();
            tblW.setW(BigInteger.valueOf(6000));
            tblW.setType(STTblWidth.PCT);
            pr.setTblW(tblW);
            tablex.setTblPr(pr);

            //centering table..
            CTJc jc = pr.addNewJc();
            jc.setVal(STJc.CENTER);
            pr.setJc(jc);

            //removing table border..
            table.getCTTbl().getTblPr().getTblBorders().setNil();
        }
        table = document.createTable(1, 4);

        widthCellsAcrossRow(table, 0, 0, 0);
        widthCellsAcrossRow(table, 0, 1, 0);
        widthCellsAcrossRow(table, 0, 2, 6000);
        widthCellsAcrossRow(table, 0, 3, 0);

        XWPFRun run = table.getRow(0).getCell(0).getParagraphs().get(0).createRun();
        run.setText(f1);
        run.setBold(true);

        run = table.getRow(0).getCell(1).getParagraphs().get(0).createRun();
        run.setText(f2);
        run.setBold(true);

        for (int i = 0; i < f3.split("\n").length; i++) {
            run = table.getRow(0).getCell(2).getParagraphs().get(0).createRun();
            if (i != 0) {
                //but don't add break if image is next... coz its added by default..
                if (!f3.split("\n")[i].trim().startsWith("IMG(")) {
                    run.addBreak();
                }
            }
            //if imageline..
            if (f3.split("\n")[i].trim().startsWith("IMG(")) {
                try {
                    //print image
                    String imgFile = "images/" + classx + "/" + subject + "/" + f3.split("\n")[i].substring(0, f3.split("\n")[i].length() - 1).trim().replace("IMG(", "");
                    BufferedImage bimg = ImageIO.read(new File(imgFile));
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();
                    int scaledWidth = getScaledWidth(width, height);
                    int scaledHeight = getScaledHeight(width, height);
                    FileInputStream is = new FileInputStream(imgFile);
//                    run.addBreak();

                    run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(scaledWidth), Units.toEMU(scaledHeight)); // 200x200 pixels
                    is.close();
                    continue;
                } catch (IOException ex) {
                    Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidFormatException ex) {
                    Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (heading) {
                table.getRow(0).getCell(2).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
                run.setBold(true);
            }
            //if marks are present, then its a question type... should be bold...
            if (f4.length() > 0) {
                run.setBold(true);
            }
            run.setText(f3.split("\n")[i]);
        }
        run = table.getRow(0).getCell(3).getParagraphs().get(0).createRun();
        run.setText(f4);
        run.setBold(true);

        //setting table size..
        CTTbl tablex = table.getCTTbl();
        CTTblPr pr = tablex.getTblPr();
        CTTblWidth tblW = pr.getTblW();
        tblW.setW(BigInteger.valueOf(6000));
        tblW.setType(STTblWidth.PCT);
        pr.setTblW(tblW);
        tablex.setTblPr(pr);

        //centering table..
        CTJc jc = pr.addNewJc();
        jc.setVal(STJc.CENTER);
        pr.setJc(jc);

        //removing table border..
        table.getCTTbl().getTblPr().getTblBorders().setNil();
    }

    private int getScaledWidth(int width, int height) {
        if (width <= 500) {
            return (int) (width * 0.777);
        }
        return 500;
    }

    private int getScaledHeight(int width, int height) {
        if (width <= 500) {
            return (int) (height * 0.777);
        }
        System.out.println("height: " + height);
        System.out.println("width: " + width);
        float ratio = (float) height / (float) width;
        System.out.println("ratio: " + ratio);
        System.out.println((int) (500 * ratio));

        return ((int) (500 * ratio * 0.777));
    }

    private String ASDF(String token) {
        if (classx.equals("8th (English)")) {
            if (subject.equals("History & Civics")) {
                if (token.equals("Answer  the following questions in about 60 to 80 words")) {
                    return "Answer  the following questions in about 80 to 100 words";
                }
            }
        } else if (classx.equals("9th (English)")) {
            if (subject.equals("ICT")) {
                if (token.equals("Answer The following:")) {
                    return "Answer in brief:";
                } else if (token.equals("Answer in brief:")) {
                    return "Answer The following:";
                } else if (token.equals("Multiple choice questions ::")) {
                    return "Rewrite the  following statements  by selecting the two correct option:";
                } else if (token.equals("Multiple choice questions :")) {
                    return "Rewrite the  following statements  by selecting the correct option:";
                }
            } else if (subject.equals("Science and Technology II") || subject.equals("Science and Technology I")) {
                if (token.equals("Answer the following in brief:")) {
                    return "Answer the following in detail :";
                }
                if (token.equals("Answer the following in detail :")) {
                    return "Answer the following in brief:";
                }
            }
        } else if (classx.equals("10th (English)")) {
            if (subject.equals("History & Political Science")) {
                if (token.equals("Answer the following questions in about 25 to 30 words (Pol. Sc):")) {
                    return "Answer any ONE of  the following questions in 25 to 30 words:";
                }
            }
        }
        return token;

    }

    private String getRandomQuestionPaperFileName(String path) {
        if (path == null) {
            path = "";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date) + ".docx");
        String dir = System.getenv("LOCALAPPDATA") + "//" + papersystem.PaperSystem.dataFolderName + "//" + path;
        //check if dir doesn't exist, create it...
        File directory = new File(dir);
        System.out.println(dir);
        if (!directory.exists()) {
            System.out.println("not exists");
            directory.mkdirs();
            if (directory.exists()) {
                System.out.println("hahaha exists");
            }
        }
        String fileName = System.getenv("LOCALAPPDATA") + "//" + papersystem.PaperSystem.dataFolderName + "//" + path + dateFormat.format(date) + ".docx";

        //check if file already exists..
        File f = new File(fileName);
        while (f.exists()) {
            int random = (int) (Math.random() * Integer.MAX_VALUE);
            fileName = System.getenv("LOCALAPPDATA") + "//" + papersystem.PaperSystem.dataFolderName + "//" + path + dateFormat.format(date) + random + ".docx";
            f = new File(fileName);
        }

        return fileName;
    }

    void saveAnswerPaper() {
        String name = JOptionPane.showInputDialog(parent, "Enter Paper Name:", "", JOptionPane.QUESTION_MESSAGE);

        if (name == null) {
            return;
        }

//        if (name.isEmpty()) {
//            name = "Paper Name";
//        }
        FileOutputStream out = null;
        String questionPaperName = null;
        try {
            //Blank Document
            XWPFDocument document = new XWPFDocument();

            //Write the Document in file system
            questionPaperName = getRandomQuestionPaperFileName("savedPapers//");
            out = new FileOutputStream(new File(questionPaperName));

            addHeaderInDoc(document, false);
            for (FourFields ff : printList) {
                addTableInDoc(document, ff.f1, ff.f2, ff.f3, ff.f4, ff.heading);
            }

            if (subject.equalsIgnoreCase("Marathi") || subject.equalsIgnoreCase("Hindi") || subject.equalsIgnoreCase("Marathi Writing Skill") || subject.equalsIgnoreCase("Hindi Writing Skill")) {
                document = changeFontOfDocument(document);
            }
            document.write(out);
            System.out.println(questionPaperName + " written successully");
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        //Saving Answer Paper...
        out = null;
        String answerPaperName = null;
        try {
            //Blank Document
            XWPFDocument document = new XWPFDocument();

            //Write the Document in file system
            answerPaperName = getRandomQuestionPaperFileName("savedPapers//");
            out = new FileOutputStream(new File(answerPaperName));

            addHeaderInDoc(document, true);
            for (FourFields ff : answerList) {
                addTableInDoc(document, ff.f1, ff.f2, ff.f3, ff.f4, ff.heading);
            }
            if (subject.equalsIgnoreCase("Marathi") || subject.equalsIgnoreCase("Hindi") || subject.equalsIgnoreCase("Marathi Writing Skill") || subject.equalsIgnoreCase("Hindi Writing Skill")) {
                document = changeFontOfDocument(document);
            }
            document.write(out);
            System.out.println(answerPaperName + " written successully");
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(PaperPrinter.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        //Save in database...
        DBManager.getInstance().insertModelPaper(name, date, questionPaperName, answerPaperName,
                DBManager.getInstance().findSubjectID(subject, classx, board));

        JOptionPane.showMessageDialog(MainScreen.mainFrame, "Paper Saved Successfully.");

    }

    public void setupLoader(JFrame parent) {
        loading = new JDialog();
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("    Please wait..."), BorderLayout.CENTER);
        loading.setUndecorated(true);
        loading.getContentPane().add(p1);
        loading.setPreferredSize(new Dimension(120, 30));
        p1.setBackground(Color.white);
        loading.pack();
        loading.setLocationRelativeTo(parent);
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loading.setModalityType(Dialog.ModalityType.MODELESS);
        loading.setVisible(true);
    }

    public void hideLoader() {
        loading.dispose();
    }

    //this method changed font of document so that marathi and hindi are supported.
    private XWPFDocument changeFontOfDocument(XWPFDocument document) {
        for (XWPFParagraph p : document.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                r.setFontFamily("Mangal");
            }
        }

        for (XWPFTable tbl : document.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            r.setFontFamily("Mangal");
                        }
                    }
                }
            }
        }
        return document;
    }
    
    /**
    * This methods identifies the questions in the printList and shuffles them.    
    */
        public void shuffleQuestions(){
            
            //PaperPrinter ref = new PaperPrinter();
            int index = -1;
            int startIndex = 0;
            int endIndex = 0;
            boolean isQType = false;
            int qCount = 0;
            int temp = 0;
            
            // in the end of the paper there is no questionType , so adding this.
            printList.add(new FourFields("","","","THE END",false,false));
            
            for(FourFields ff : printList){
                index++;
                startIndex = temp;
                if(ff.f4.length() > 0 || ff.heading || ff.f3.equals("OR")){
                    temp = index+1;
                    System.out.println("startIndex: "+startIndex);
                    isQType = true;
                }
                if(ff.question){
                    isQType = false;
                    qCount++;
                    System.out.println("qcount: "+qCount);
                    continue;
                }
                if(isQType && qCount > 0){
                    endIndex = index;
                    System.out.println("---- \nendIndex: "+endIndex);
                    current.shuffle(startIndex, endIndex);
                }
            }
            qIDIndex = 0;
            printList.remove(printList.size()-1);
            // Refresh the anwserList with respect to new shuffled printList
            answerList.clear();
            questionNumber = 1;
            questionNumberPrinted = 1;
            indexForGenerateAns = 0;
            generateAnswers(patternTypes);
            // calls the PaperEditor Screen
            PaperEditor.generateEditMenu(current, printList, answerList);
        }
    
    /**
    *  The method shuffles the questions in the printList and rearrange the addedQuestionIDs list with respect to the shuffle.
    * @param startIndex takes the starting index
    * @param endIndex takes the ending index
    * @result It Shuffles all the elements in the given index range of the printList.
    */
        static int qIDIndex = 0; // remember to intialize this 0 after complete shuffle
        public void shuffle(int startIndex , int endIndex){
          
            
            ArrayList<FourFields> tempQShuffle = new ArrayList<>();
            ArrayList<String> tempQuestionIDs = new ArrayList<>();
            
            
            //Get specific question from the printlist and add them into tempQShuffe list.
            for(int i = startIndex ; i < endIndex ; i++){               
                
                if(printList.get(i).question){
                    System.out.println("Index:"+i+" ->"+printList.get(i).f3);
                    tempQShuffle.add(printList.get(i));
                }
            }
            // add corresponding question ids for the questions into the tempQuesitonID list
            for(int i = qIDIndex ; i < tempQShuffle.size() + qIDIndex; i++){
                tempQuestionIDs.add(addedQuestionIDs.get(i));
            }
            
            
            //Shuffle tempQShuffle list and addedQuestionIDs list in similar fashion
            long seed = System.nanoTime();
            Collections.shuffle(tempQShuffle, new Random(seed));
            Collections.shuffle(tempQuestionIDs, new Random(seed));
            // Formalize the sequence of the roman numerals after shuffle
            int j = 0;
            for(FourFields ff : tempQShuffle){
                ff.f2 = IntegerToRomanNumeral(j+1);
                j++;
            }
            // Add the shuffled qids into the addedQuestionIDs list
            int in = 0; // index to iterate tempQID list
            for(int i = qIDIndex ; i < tempQShuffle.size() + qIDIndex; i++){
                addedQuestionIDs.set(i,tempQuestionIDs.get(in));
                in++;
            }
            qIDIndex += tempQShuffle.size();
            // replace questions of printList with tempQShuffle questions 
            in = 0;
            for(int i = startIndex ; i < endIndex ; i++){               
                printList.set(i, tempQShuffle.get(in));
                in++;
            }
            
            
            
            System.out.println(printList.size()-1);
            System.out.println("Kashan --- Length Check \n"+tempQShuffle.size()+"="+tempQuestionIDs.size()+"=QID "+addedQuestionIDs.size());
            //PaperEditor.generateEditMenu(current, printList, answerList);
        }

}