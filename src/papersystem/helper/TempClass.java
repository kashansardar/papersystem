/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem.helper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

/**
 *
 * @author Abbasi
 */
public class TempClass {

    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidFormatException, BiffException {
//        Files.walk(Paths.get("C:\\Users\\Abbasi\\Documents\\NetBeansProjects\\PaperSystem\\images\\8th (English)\\Mathematics")).forEach(filePath -> {
//            if (Files.isRegularFile(filePath)) {
//                try {
//                    String name = filePath.getFileName().toString().replace("image","").replace(".gif","");
//                    name = "image" + String.format("%03d", Integer.parseInt(name)) + ".gif";
//                    renameFile(new File(filePath.toString()), name);
//                    System.out.println(name);
//                } catch (IOException ex) {
//                    Logger.getLogger(TempClass.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        });
//        findImagesAndCopy();
//        findChapterErrors2();
//        renameImagesCorrectly();
//        checkImages();
//        checkIMGTextInQuestionList();
        findQuestionTypeErrorsInQuestionList();
    }

    private static void findChapterErrors() throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File("Chapters List Final.xls"));
        Sheet sheet = workbook.getSheet(0);

        ArrayList<String> chaptersList = new ArrayList<>();
        ArrayList<String> chaptersListQ = new ArrayList<>();
        ArrayList<String> errorList = new ArrayList<>();
        //Skips first line...
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell chapterCell = sheet.getCell(0, i);
            //incase of empty row skip                 
            String chapter = chapterCell.getContents().trim();
            if (chapter.length() == 0) {
                continue;
            }
            chaptersList.add(chapter);
        }
        workbook.close();

        workbook = Workbook.getWorkbook(new File("Questions List Final.xls"));
        sheet = workbook.getSheet(0);
        //Skips first line...
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell chapterCell = sheet.getCell(3, i);
            //incase of empty row skip                 
            String chapter = chapterCell.getContents().trim().replace(".docx", "");
            if (chapter.length() == 0) {
                continue;
            }
            chaptersListQ.add(chapter);
        }
        workbook.close();

        for (String check : chaptersListQ) {
            boolean error = true;
            for (String check2 : chaptersList) {
                if (check2.compareToIgnoreCase(check) == 0) {
                    error = false;
                    break;
                }
            }
            if (error) {

                boolean alreadyAdded = false;
                for (String s : errorList) {
                    if (s.equals(check)) {
                        alreadyAdded = true;
                    }
                }
                if (!alreadyAdded) {
                    errorList.add(check);
                    System.out.println(check);
                }
            }
        }
    }
    
    private static void findChapterErrors2() throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File("Chapters List Final.xls"));
        Sheet sheet = workbook.getSheet(0);

        ArrayList<String> chaptersList = new ArrayList<>();
        ArrayList<String> chaptersListQ = new ArrayList<>();
        ArrayList<String> errorList = new ArrayList<>();
        //Skips first line...
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell chapterCell = sheet.getCell(0, i);
            //incase of empty row skip                 
            String chapter = chapterCell.getContents().trim();
            if (chapter.length() == 0) {
                continue;
            }
            chaptersList.add(chapter);
        }
        workbook.close();

        workbook = Workbook.getWorkbook(new File("Questions List Final.xls"));
        sheet = workbook.getSheet(0);
        //Skips first line...
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell chapterCell = sheet.getCell(3, i);
            //incase of empty row skip                 
            String chapter = chapterCell.getContents().trim().replace(".docx", "");
            if (chapter.length() == 0) {
                continue;
            }
            chaptersListQ.add(chapter);
        }
        workbook.close();

        for (String check : chaptersList) {
            boolean error = true;
            for (String check2 : chaptersListQ) {
                if (check2.compareToIgnoreCase(check) == 0) {
                    error = false;
                    break;
                }
            }
            if (error) {

                boolean alreadyAdded = false;
                for (String s : errorList) {
                    if (s.equals(check)) {
                        alreadyAdded = true;
                    }
                }
                if (!alreadyAdded) {
                    errorList.add(check);
                    System.out.println(check);
                }
            }
        }
    }

    private static void findImagesAndCopy() {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("tempp.xls"), ws);
            Sheet sheet = workbook.getSheet(0);

            int id = 0;
            int totalRows = sheet.getRows();
            //Skips first line...
            ArrayList<String> addresses = new ArrayList<>();
            int hiya = 0;
            for (int i = 1; i < totalRows; i++) {
                Cell questionCell = sheet.getCell(0, i);
                //incase of empty row skip                 
                String question = questionCell.getContents().trim();
                if (question.length() == 0) {
                    continue;
                }
                Cell answerCell = sheet.getCell(1, i);
                String answer = answerCell.getContents().trim();
                Cell qTypeCell = sheet.getCell(2, i);
                String qType = qTypeCell.getContents().trim();
                Cell chapterCell = sheet.getCell(3, i);
                String chapter = chapterCell.getContents().trim().replace(".docx", "");
                Cell subjectCell = sheet.getCell(4, i);
                String subject = subjectCell.getContents().trim();
                Cell classCell = sheet.getCell(5, i);
                String classx = classCell.getContents().trim();

                while (question.contains("IMG(")) {
                    question = question + "\n";
                    question = question.substring(question.indexOf("IMG(") + 4);
                    String address = question.substring(0, question.indexOf(")\n"));
                    System.out.println(address + ++hiya);

                    File src = new File(address.replace("\\ ", "\\"));
                    File dest = new File("C:\\Users\\Abbasi\\Desktop\\temp 11th com images\\" + subject + "\\" + src.getName());
//                    System.out.println("Src: " + src + "\nDest: " + dest);
                    copyFileUsingChannel(src, dest);
                    question = question.substring(question.indexOf(")\n") + 1);
                }
                while (answer.contains("IMG(")) {
                    answer = answer + "\n";
                    answer = answer.substring(answer.indexOf("IMG(") + 4);
                    String address = answer.substring(0, answer.indexOf(")\n"));
                    System.out.println(address + ++hiya);
                    File src = new File(address.replace("\\ ", "\\"));
                    File dest = new File("C:\\Users\\Abbasi\\Desktop\\temp 11th com images\\" + subject + "\\" + src.getName());
//                    System.out.println("Src: " + src + "\nDest: " + dest);
                    copyFileUsingChannel(src, dest);

                    answer = answer.substring(answer.indexOf(")\n") + 1);
                }
            }
            workbook.close();

        } catch (IOException ex) {
            Logger.getLogger(papersystem.helper.DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(papersystem.helper.DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            destChannel.close();
        }
    }

    public static void renameFile(File toBeRenamed, String new_name)
            throws IOException {
        //need to be in the same path
        File fileWithNewName = new File(toBeRenamed.getParent(), new_name);
        if (fileWithNewName.exists()) {
            throw new IOException("file exists");
        }
        // Rename file (or directory)
        boolean success = toBeRenamed.renameTo(fileWithNewName);
        if (!success) {
            // File was not successfully renamed
        }
    }

    public static void listf(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
//                System.out.println(file.getName());
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath(), files);
            }
        }
    }

    public static void renameImagesCorrectly()
            throws IOException {

        ArrayList<File> files = new ArrayList<>();
        File directory = new File("C:\\Users\\Abbasi\\Documents\\NetBeansProjects\\PaperSystem\\images\\9th (English)");
        listf(directory.getAbsolutePath(), files);
        System.out.println(files.size());

        for (File f : files) {
            if (f.getName().contains(".jpg)")) {
                System.out.println(f.getName());
//                System.out.println(f.getName().replace("(IMG ", "IMG("));

                File fileWithNewName = new File(f.getParent(), f.getName().replace(".jpg)", ".jpg"));
                f.renameTo(fileWithNewName);
            }
        }
//        return;

    }

    //this functions reads quetion files and check all images present in it are available or not..
    public static void checkImages() throws IOException, BiffException {
        Workbook workbook;
        workbook = Workbook.getWorkbook(new File("Questions List Final.xls"));
        Sheet sheet = workbook.getSheet(0);

        //Skips first line...
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell questionCell = sheet.getCell(0, i);
            Cell answerCell = sheet.getCell(1, i);
            Cell chapterCell = sheet.getCell(3, i);
            Cell subjectCell = sheet.getCell(4, i);
            Cell classCell = sheet.getCell(5, i);
            //incase of empty row skip                 
            String question = questionCell.getContents().trim();
            String answer = answerCell.getContents().trim();
            String chapter = chapterCell.getContents().trim().replace(".docx", "");
            String subject = subjectCell.getContents().trim();
            String classx = classCell.getContents().trim();
            if (question.length() == 0) {
                continue;
            }

            for (String s : question.split("\n")) {
                if (s.startsWith("IMG(")) {
                    //find image using subject class address path
                    String imgFile = "images/" + classx + "/" + subject + "/" + s.substring(0, s.length() - 1).replace("IMG(", "");
                    if (!(new File(imgFile).exists())) {
                        System.out.println(imgFile);
                    };
                }
            }
        }
        workbook.close();

    }

    //this functions reads each question and answer to find if any lines has IMG(xxx) mixed with other text
    public static void checkIMGTextInQuestionList() throws IOException, BiffException {
        Workbook workbook;
        workbook = Workbook.getWorkbook(new File("Questions List Final.xls"));
        Sheet sheet = workbook.getSheet(0);

        //Skips first line...
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell questionCell = sheet.getCell(0, i);
            Cell answerCell = sheet.getCell(1, i);
            //incase of empty row skip                 
            String question = questionCell.getContents().trim();
            String answer = answerCell.getContents().trim();
            if (question.length() == 0) {
                continue;
            }

            for (String s : question.split("\n")) {
                s = s.trim();
                if (s.startsWith("IMG(")) {
                    if (!s.endsWith(")")) {
                        System.out.println("Row " + (i + 1) + ": Question");
                    }
                } else if (s.contains("IMG(")) {
                    System.out.println("Row " + (i + 1) + ": Question");
                }
            }

            for (String s : answer.split("\n")) {
                s = s.trim();
                if (s.startsWith("IMG(")) {
                    if (!s.endsWith(")")) {
                        System.out.println("Row " + (i + 1) + ": Answer");
                    }
                } else if (s.contains("IMG(")) {
                    System.out.println("Row " + (i + 1) + ": Answer");
                }
            }
        }
        workbook.close();

    }

    private static void findQuestionTypeErrorsInQuestionList() throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File("Question Types List Final.xls"));
        Sheet sheet = workbook.getSheet(0);

        ArrayList<QTypeSubjectClass> qTypeList = new ArrayList<>();
        ArrayList<QTypeSubjectClass> qTypeListQ = new ArrayList<>(); //qtypes in Question list file
        ArrayList<QTypeSubjectClass> errorList = new ArrayList<>();
        //Skips first line...

        for (int i = 1; i < sheet.getRows(); i++) {
            Cell qTypeCell = sheet.getCell(0, i);
            Cell subjectCell = sheet.getCell(2, i);
            //incase of empty row skip                 
            String qType = qTypeCell.getContents().trim();
            String subject = subjectCell.getContents().trim();
            if (qType.length() == 0) {
                continue;
            }

            qTypeList.add(new QTypeSubjectClass(subject, qType));
        }
        workbook.close();

        workbook = Workbook.getWorkbook(new File("Questions List Final.xls"));
        sheet = workbook.getSheet(0);
        //Skips first line...
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell questionCell = sheet.getCell(0, i);
            Cell qTypeCell = sheet.getCell(2, i);
            Cell subjectCell = sheet.getCell(4, i);
            //incase of empty row skip                 
            String question = questionCell.getContents().trim();
            String qType = qTypeCell.getContents().trim();
            String subject = subjectCell.getContents().trim();
            if (question.length() == 0) {
                continue;
            }
            qTypeListQ.add(new QTypeSubjectClass(subject, qType));
        }
        workbook.close();

                    
                    
        for (QTypeSubjectClass check : qTypeListQ) {
            boolean error = true;
            for (QTypeSubjectClass check2 : qTypeList) {
                if (check2.equals(check)) {
                    error = false;
                    break;                   
                }
            }
            if (error) {
                boolean alreadyAdded = false;
                for (QTypeSubjectClass check2 : errorList) {
                    if (check2.equals(check)) {
                        alreadyAdded = true;
                    }
                }
                if (!alreadyAdded) {
                    errorList.add(check);
                    System.out.println(check.subject + "\n" + check.qType + "\n");
                }
            }
        }
    }

}

class QTypeSubjectClass {

    String subject;
    String qType;

    public QTypeSubjectClass(String subject, String qType) {
        this.subject = subject;
        this.qType = qType;
    }

    public boolean equals(QTypeSubjectClass qType) {
        if (qType.subject.equals(this.subject)
                && qType.qType.equals(this.qType)) {
            return true;
        }
        return false;
    }
}
