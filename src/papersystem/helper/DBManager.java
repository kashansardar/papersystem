package papersystem.helper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.StringUtils;

//Singleton Class...
public class DBManager {

    private static DBManager instance = null;
    private Statement stmt = null;
    private Connection c = null;
//    String[] classes = {"12th Science", "12th Commerce", "11th Science", "11th Commerce", "10th (English)", "9th (English)", "8th (English)"};
    //String[] classes = {"12th Science", "11th Science"};

    private DBManager() {
        // Exists only to defeat instantiation.
        createDataBase();
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public ArrayList<String> getClasses(String board) {
        //Default option..
        if (board == null) {
            try {
                ArrayList<String> temp = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT CLASS FROM SUBJECT;");
                //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("GETTING CLASSES FROM DATABASE:::");
                while (rs.next()) {
                    String classx = rs.getString("class");
                    temp.add(classx);
                    //System.out.println(classx);
                }
                //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                rs.close();

                return temp;
            } //Federal Board...
            catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
                return null;
            }
        } else if (board.compareTo("fbise") == 0) {
            return null;
        } else {
            return null;
        }
    }

    public ArrayList<String> getSubjects(String board, String classx) {
        //Default option..
        if (board == null) {
            try {
                //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("GETTING SUBJECTS FOR" + classx + " FROM DATABASE:::");
                PreparedStatement p = c.prepareStatement("SELECT NAME FROM SUBJECT WHERE CLASS=?;");
                p.setString(1, classx);
                ResultSet rs = p.executeQuery();
                //ResultSet rs = stmt.executeQuery("SELECT NAME FROM SUBJECT WHERE CLASS=;");
                ArrayList<String> temp = new ArrayList<>();
                while (rs.next()) {
                    String name = rs.getString("name");
                    temp.add(name);
                    //System.out.println(name);
                }
                //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                rs.close();
                return temp;
            } //Federal Board...
            catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
                return null;
            }
        } else if (board.compareTo("fbise") == 0) {
            return null;
        } else {
            return null;
        }
    }

    public ArrayList<String> getChapters(String board, String classx, String subject) {
        //Default option..
        if (board == null) {
            try {
                ArrayList<String> temp = new ArrayList<>();
                board = "default";
                int subjectID = findSubjectID(subject, classx, board);

                PreparedStatement p = c.prepareStatement(""
                        + "SELECT CHAPTER.NAME AS NAME "
                        + "FROM CHAPTER INNER JOIN SUBJECT ON CHAPTER.SUBJECT=SUBJECT.ID "
                        + "WHERE SUBJECT.ID=?;");
                p.setInt(1, subjectID);
                ResultSet rs = p.executeQuery();

                //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("GETTING CHAPTERS FOR " + subject + " " + classx + " FROM DATABASE:::");
                while (rs.next()) {
                    String name = rs.getString("name");
                    //System.out.println(name);
                    temp.add(name);
                }
                //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                rs.close();
                return temp;
            } //Federal Board...
            catch (Exception ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
                return null;
            }
        } else if (board.compareTo("fbise") == 0) {
            return null;
        } else {
            return null;
        }
    }

    //returns all paper patterns for the given subject
    public ArrayList<String> getPaperPatterns(int subjectID) {

        try {
            ArrayList<String> temp = new ArrayList<>();

            PreparedStatement p = c.prepareStatement(""
                    + "SELECT NAME "
                    + "FROM PAPERPATTERNS  "
                    + "WHERE SUBJECT=?;");
            p.setInt(1, subjectID);
            ResultSet rs = p.executeQuery();

            //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("GETTING Patterns FOR Subject ID: " + subjectID + " FROM DATABASE:::");
            while (rs.next()) {
                String name = rs.getString("name");
                //System.out.println(name);
                temp.add(name);
            }
            //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            rs.close();
            return temp;
        } //Federal Board...
        catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //Deletes previous database tables and creates new...
    private void createDataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

//            createTables(stmt);
//            addSubjectValuesExcel(stmt);
//            addChapterValuesExcel();
//            addQuestionTypeValuesExcel();
//            addPaperPatternsValuesExcel();
////            addPreviousPaperValuesExcel();
//
//            createQuestionTables(stmt);
//            addQuestionValuesExcel();
            //printSubjectTable(stmt);    
            //printChaptersTable();
            //printQuestionTypesTable();
            //stmt.close();
            //c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Query ran successfully");
    }

    //don't call before adding subjects in database...
    private void createQuestionTables(Statement stmt) throws SQLException {

        for (String c : getClasses(null)) {
            for (String s : getSubjects(null, c)) {
                String abc = "DROP TABLE IF EXISTS QUESTIONS" + filterClassSubjectName(c) + filterClassSubjectName(s);
                stmt.executeUpdate(abc);
            }
        }

        for (String c : getClasses(null)) {
            for (String s : getSubjects(null, c)) {
                String sql = "CREATE TABLE IF NOT EXISTS QUESTIONS" + filterClassSubjectName(c) + filterClassSubjectName(s) + " "
                        + "(ID INT PRIMARY KEY     NOT NULL,"
                        + " QUESTION       TEXT    NOT NULL, "
                        + " ANSWER         TEXT    NOT NULL, "
                        + " TYPE           TEXT    NOT NULL, "
                        + " CHAPTER        INT     NOT NULL)";
                stmt.executeUpdate(sql);
            }
        }
    }

    private void createTables(Statement stmt) throws SQLException {
        //If you don't clean a table dont create it in create database!!!!!!!!!!
        String abc = "DROP TABLE IF EXISTS SUBJECT";
        stmt.executeUpdate(abc);
        abc = "DROP TABLE IF EXISTS CHAPTER";
        stmt.executeUpdate(abc);
        abc = "DROP TABLE IF EXISTS QUESTIONTYPES";
        stmt.executeUpdate(abc);
        abc = "DROP TABLE IF EXISTS QUESTIONS";
        stmt.executeUpdate(abc);
        abc = "DROP TABLE IF EXISTS MODELPAPERS";
        stmt.executeUpdate(abc);
        abc = "DROP TABLE IF EXISTS PAPERPATTERNS";
        stmt.executeUpdate(abc);

        String sql = "CREATE TABLE IF NOT EXISTS SUBJECT "
                + "(ID INT PRIMARY KEY     NOT NULL,"
                + " NAME           TEXT    NOT NULL, "
                + " CLASS          TEXT    NOT NULL, "
                + " BOARD          TEXT    NOT NULL)";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS CHAPTER "
                + "(ID INT PRIMARY KEY     NOT NULL,"
                + " NAME           TEXT    NOT NULL, "
                + " SUBJECT        INT     NOT NULL)";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS QUESTIONTYPES "
                + "(ID INT PRIMARY KEY     NOT NULL,"
                + " NAME           TEXT    NOT NULL, "
                + " MARKS          INT     NOT NULL, "
                + " SUBJECT        INT     NOT NULL)";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS PAPERPATTERNS "
                + "(ID INT PRIMARY KEY     NOT NULL, "
                + " NAME           TEXT    NOT NULL, "
                + " TYPES          TEXT    NOT NULL, "
                + " QTOASK         TEXT    NOT NULL, "
                + " QTOANS         TEXT    NOT NULL, "
                + " MARKS          TEXT    NOT NULL, "
                + " SUBJECT        INT     NOT NULL, "
                + " INFORMATION    TEXT    NOT NULL, "
                + " FIXED          TEXT    NOT NULL, "
                + " TOTALMARKS     TEXT    NOT NULL)";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS MODELPAPERS "
                + "(ID INT PRIMARY KEY     NOT NULL, "
                + " NAME           TEXT    NOT NULL, "
                + " DATE           TEXT    NOT NULL, "
                + " QFILE          TEXT    NOT NULL, "
                + " AFILE          TEXT    NOT NULL, "
                + " SUBJECT        INT     NOT NULL)";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS PREVIOUSPAPERS "
                + "(ID INT PRIMARY KEY     NOT NULL, "
                + " FILE           TEXT    NOT NULL, "
                + " YEAR           TEXT    NOT NULL, "
                + " SUBJECT        INT     NOT NULL)";
        stmt.executeUpdate(sql);
    }

    private void addSubjectValuesExcel(Statement stmt) throws SQLException {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("Subjects List Final.xls"), ws);
            Sheet sheet = workbook.getSheet(0);

            int id = 0;
            //Skips first line...
            for (int i = 1; i < sheet.getRows(); i++) {
                Cell classCell = sheet.getCell(0, i);
                //incase of empty row skip                 
                String classx = classCell.getContents().trim();
                if (classx.length() == 0) {
                    continue;
                }
                Cell subjectCell = sheet.getCell(1, i);
                String subject = subjectCell.getContents().trim();
                //System.out.println(classx + " ------------ " + subject);

                PreparedStatement p = c.prepareStatement("INSERT INTO SUBJECT (ID,NAME,CLASS,BOARD) "
                        + "VALUES (?, ?, ?, ?);");
                p.setInt(1, id++);
                p.setString(2, subject);
                p.setString(3, classx);
                p.setString(4, "default");
                p.execute();
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

    private void addChapterValuesExcel() throws SQLException {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("Chapters List Final.xls"), ws);
            Sheet sheet = workbook.getSheet(0);

            int id = 0;
            //Skips first line...
            for (int i = 1; i < sheet.getRows(); i++) {
                Cell chapterCell = sheet.getCell(0, i);
                //incase of empty row skip                 
                String chapter = chapterCell.getContents().trim();
                if (chapter.length() == 0) {
                    continue;
                }
                Cell subjectCell = sheet.getCell(1, i);
                String subject = subjectCell.getContents().trim();
                Cell classCell = sheet.getCell(2, i);
                String classx = classCell.getContents().trim();
                System.out.println(chapter + " ------------ " + subject + " ------------ " + classx);

                //Find Subject ID from Subject table...
                int subjectID = -1;
                try {
                    subjectID = findSubjectID(subject, classx, "default");
                } catch (Exception ex) {

                    Logger.getLogger(papersystem.helper.DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(0);
                }

                PreparedStatement p = c.prepareStatement("INSERT INTO CHAPTER (ID,NAME,SUBJECT) VALUES (?, ?, ?);");
                p.setInt(1, id++);
                p.setString(2, chapter);
                p.setInt(3, subjectID);
                p.execute();

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

    private void addQuestionTypeValuesExcel() throws SQLException {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("Question Types List Final.xls"), ws);
            Sheet sheet = workbook.getSheet(0);

            int id = 0;
            int totalRows = sheet.getRows();
            //Skips first line...
            for (int i = 1; i < totalRows; i++) {
                Cell typeNameCell = sheet.getCell(0, i);
                //incase of empty row skip                 
                String typeName = typeNameCell.getContents().trim();
                if (typeName.length() == 0) {
                    continue;
                }
                Cell marksCell = sheet.getCell(1, i);
                String marks = marksCell.getContents().trim();
                int marksInteger = Integer.parseInt(marks);
                Cell subjectCell = sheet.getCell(2, i);
                String subject = subjectCell.getContents().trim();
                Cell classCell = sheet.getCell(3, i);
                String classx = classCell.getContents().trim();
                System.out.println(typeName + " ------------ " + marks + " ------------ " + subject + "[" + classx + "]");

                //Find Subject ID from Subject table...
                int subjectID = -1;
                try {
                    subjectID = findSubjectID(subject, classx, "default");
                } catch (Exception ex) {
                    Logger.getLogger(papersystem.helper.DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(0);
                }

                PreparedStatement p = c.prepareStatement("INSERT INTO QUESTIONTYPES (ID,NAME,MARKS,SUBJECT) "
                        + "VALUES (?, ?, ?, ?);");
                p.setInt(1, id++);
                p.setString(2, typeName);
                p.setInt(3, marksInteger);
                p.setInt(4, subjectID);
                p.execute();

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

    private void addQuestionValuesExcel() throws SQLException {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("Questions List Final.xls"), ws);
            Sheet sheet = workbook.getSheet(0);

            int id = 0;
            int totalRows = sheet.getRows();
            //Skips first line...
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
                System.out.println("Row: " + i + "\n" + question + " ------------ " + answer + " ------------ " + qType + " ------------ " + chapter + " ------------ " + subject + " ------------ " + classx);

                //Find Subject ID from Subject table...                
                //Only executed for the first row because each file is for one subject only...
                int chapterID = -1;
                try {
                    int subjectID = findSubjectID(subject, classx, "default");
                    chapterID = findChapterID(chapter, subjectID);
                } catch (Exception ex) {
                    Logger.getLogger(papersystem.helper.DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(0);
                }

                PreparedStatement p = c.prepareStatement(""
                        + "INSERT INTO QUESTIONS" + filterClassSubjectName(classx) + filterClassSubjectName(subject)
                        + " (ID,QUESTION,ANSWER,TYPE,CHAPTER) "
                        + "VALUES (?, ?, ?, ?, ?);");
                p.setInt(1, id++);
                p.setString(2, question);
                p.setString(3, answer);
                p.setString(4, qType);
                p.setInt(5, chapterID);
                p.execute();

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

    private void addPaperPatternsValuesExcel() throws SQLException {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("Paper Patterns List.xls"), ws);
            Sheet sheet = workbook.getSheet(0);

            int id = 0;
            int totalRows = sheet.getRows();
            //Skips first line...
            for (int i = 1; i < totalRows; i++) {
                Cell nameCell = sheet.getCell(0, i);
                String name = nameCell.getContents().trim();
                //incase of empty row skip
                if (name.length() == 0) {
                    continue;
                }
                Cell typesCell = sheet.getCell(1, i);
                String types = typesCell.getContents().trim();
                Cell qToAskCell = sheet.getCell(2, i);
                String qToAsk = qToAskCell.getContents().trim();
                Cell qToAnsCell = sheet.getCell(3, i);
                String qToAns = qToAnsCell.getContents().trim();
                Cell marksCell = sheet.getCell(4, i);
                String marks = marksCell.getContents().trim();
                Cell subjectCell = sheet.getCell(5, i);
                String subject = subjectCell.getContents().trim();
                Cell classCell = sheet.getCell(6, i);
                String classx = classCell.getContents().trim();
                Cell infoCell = sheet.getCell(7, i);
                String info = infoCell.getContents().trim();
                Cell fixedCell = sheet.getCell(8, i);
                String fixed = fixedCell.getContents().trim();
                Cell totalMarksCell = sheet.getCell(9, i);
                String totalMarks = totalMarksCell.getContents().trim();
                System.out.println(name + " ------------ " + types + " ------------ " + qToAsk + " ------------ " + qToAns + " ------------ " + marks + " ------------ " + classx + " ------------ " + info + " ------------ " + fixed + " ------------ " + totalMarks);

                //Find Subject ID from Subject table...                                
                int subjectID = -1;
                try {
//                    System.out.println("temp: "+ subject+"  "+classx);
                    subjectID = findSubjectID(subject, classx, "default");
                } catch (Exception ex) {
                    Logger.getLogger(papersystem.helper.DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(0);
                }

                PreparedStatement p = c.prepareStatement(""
                        + "INSERT INTO PAPERPATTERNS (ID, NAME, TYPES, QTOASK, QTOANS, MARKS, SUBJECT, INFORMATION, FIXED, TOTALMARKS) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                p.setInt(1, id++);
                p.setString(2, name);
                p.setString(3, types);
                p.setString(4, qToAsk);
                p.setString(5, qToAns);
                p.setString(6, marks);
                p.setInt(7, subjectID);
                p.setString(8, info);
                p.setString(9, fixed);
                p.setString(10, totalMarks);
                p.execute();

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

    //returns id of the first subject with given name, class, and board....
    public int findSubjectID(String subjectName, String classx, String board) {
        if (board == null) {
            board = "default";
        }
        try {
            ResultSet rs = stmt.executeQuery("SELECT ID FROM SUBJECT WHERE "
                    + "UPPER(NAME)  =  UPPER('" + subjectName + "') AND "
                    + "UPPER(CLASS) =  UPPER('" + classx + "') AND "
                    + "UPPER(BOARD) =  UPPER('" + board + "');");

            while (rs.next()) {
                int id = rs.getInt("ID");
                return id;
            }
            throw new Exception("Subject Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        }
    }

    //returns id of the first chapter with given chapter name & subject id....
    public int findChapterID(String chapterName, int subjectID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT ID FROM CHAPTER WHERE "
                    + "UPPER(NAME)  =  UPPER(?) AND "
                    + "UPPER(SUBJECT) =  UPPER(?) "
                    + "LIMIT 1;");
            p.setString(1, chapterName);
            p.setInt(2, subjectID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                return id;
            }
            throw new Exception("Chapter Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        }
    }

    //returns id of the first pattern with given pattern name & subject id....
    public int findPatternID(String patternName, int subjectID) {
        System.out.println("LOG::: Finding Pattern ID for pattern(" + patternName + ") and subject id(" + subjectID + ")");
        try {
            PreparedStatement p = c.prepareStatement("SELECT ID FROM PAPERPATTERNS WHERE "
                    + "UPPER(NAME)  =  UPPER(?) AND "
                    + "UPPER(SUBJECT) =  UPPER(?) "
                    + "LIMIT 1;");
            p.setString(1, patternName);
            p.setInt(2, subjectID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                return id;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        }
    }

    //returns id of the first QuestionType with given type name & subject id....
    public int findQuestionTypeID(String qTypeName, int subjectID) {
        System.out.println("LOG::: Finding Question Type ID for pattern(" + qTypeName + ") and subject id(" + subjectID + ")");
        int result = -1;
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM QUESTIONTYPES WHERE "
                    + "UPPER(NAME)  =  UPPER(?) AND "
                    + "UPPER(SUBJECT)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setString(1, qTypeName);
            p.setInt(2, subjectID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                result = rs.getInt("ID");
                return result;
            }
            throw new Exception("Type Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        }
    }

    //returns types value given paperPattern id....
    public String getPatternTypes(int paperPatternID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT TYPES FROM PAPERPATTERNS WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, paperPatternID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String types = rs.getString("TYPES");
                return types;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns toAsk value given pattern id....
    public String getPatternToAsk(int patternID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM PAPERPATTERNS WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, patternID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("QTOASK");
                return result;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns toAns value given pattern id....
    public String getPatternToAns(int patternID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM PAPERPATTERNS WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, patternID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("QTOANS");
                return result;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns marks value given pattern id....
    public String getPatternMarks(int patternID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM PAPERPATTERNS WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, patternID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("MARKS");
                return result;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    /**
     * returns question ids against given type, marks and chapter.... 2nd
     * version of the function... used to overcome the problem with duplicate
     * type names in one subject..
     *
     * @param qType a valid qType present in database
     * @param chapterID id value from chapter table
     * @param marks marks of qType
     * @return an array list containing all question ids with given type in the
     * chapter...
     */
    private ArrayList<Integer> getQuestionIDs(String qType, int chapterID, int marks, String classx, int subID) {
        ArrayList<Integer> qIDs = new ArrayList<>();
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(getSubjectName(subID))
                    + " WHERE "
                    + "UPPER(TYPE)  =  UPPER(?) AND "
                    + "UPPER(MARKS)  =  UPPER(?) AND "
                    + "UPPER(CHAPTER)  =  UPPER(?);");
            p.setString(1, qType);
            p.setInt(2, marks);
            p.setInt(3, chapterID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                qIDs.add(Integer.parseInt(rs.getString("ID")));
            }
            return qIDs;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    /**
     * returns question ids against given type, chapter....
     *
     * @param qType a valid qType name present in database
     * @param chapterID id value from chapter table
     * @return an array list containing all question ids with given type in the
     * chapter...
     */
    private ArrayList<Integer> getQuestionIDs(String qType, int chapterID, String classx, int subID) {
        ArrayList<Integer> qIDs = new ArrayList<>();
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(getSubjectName(subID))
                    + " WHERE "
                    + "UPPER(TYPE)  =  UPPER(?) AND "
                    + "UPPER(CHAPTER)  =  UPPER(?);");
            p.setString(1, qType);
            p.setInt(2, chapterID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                qIDs.add(Integer.parseInt(rs.getString("ID")));
            }
            return qIDs;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns question against given question id....
    public String getQuestion(int questionID, String classx, String subject) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(subject)
                    + " WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, questionID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("QUESTION");
                return result;
            }
            throw new Exception("Question Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns answer against given question id....
    public String getAnswer(int questionID, String classx, String subject) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(subject)
                    + " WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, questionID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("ANSWER");
                if (result.equalsIgnoreCase("void")) {
                    return "";
                }
                return result;
            }
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns list of questions against list of given question ids....
    //empty if none found or if no ids sent
    public ArrayList<String> getQuestionsByIDs(ArrayList<Integer> questionIDs, String classx, String subject) {
        ArrayList<String> result = new ArrayList<>();

        if (questionIDs.isEmpty()) {
            return result;
        }
        try {
            String statement = "DROP TABLE IF EXISTS TEMPTABLE";

            statement = "CREATE TABLE IF NOT EXISTS TEMPTABLE"
                    + "(ID INT PRIMARY KEY     NOT NULL)";
            stmt.executeUpdate(statement);

            statement = "DELETE FROM TEMPTABLE";
            stmt.executeUpdate(statement);

            for (int i = 0; i < questionIDs.size(); i++) {
                statement = "INSERT INTO TEMPTABLE "
                        + " (ID) "
                        + " VALUES (?);";
                PreparedStatement p = c.prepareStatement(statement);
                p.setInt(1, questionIDs.get(i));
                p.executeUpdate();
            }

            statement = "SELECT * FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(subject)
                    + " qt, TEMPTABLE tt "
                    + " WHERE qt.id = tt.id";
            PreparedStatement p = c.prepareStatement(statement);
            ResultSet rs = p.executeQuery();

            Hashtable<Integer, String> values = new Hashtable<>();
            while (rs.next()) {
                values.put(rs.getInt("ID"), rs.getString("QUESTION"));
            }
            for (int x = 0; x < questionIDs.size(); x++) {
                result.add(values.get(questionIDs.get(x)));
            }
            System.out.println("LOG:: 123 : Input QIDs " + questionIDs.size() + " Output Questions " + result.size());
            return result;
            //throw new Exception("Question ID Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    public void printAllPatterns() {
        try {
            PreparedStatement p = c.prepareStatement("SELECT TYPES FROM PAPERPATTERNS;");
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String types = rs.getString("TYPES");
                System.out.println(types);
            }
            //throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    /**
     * returns question id of a random question for given question types, from
     * given subject id in given chapters, that is not present in
     * presentQuestionIDs returns -1, if doesn't find any valid results,
     *
     * @param qType Name value of question type..
     * @param subjectID id of subject..
     * @param chapters list of chapters to select from
     * @param presentQuestionIDs list of questions to exclude...
     * @return
     */
    //
    int getRandomQuestion(String qType, int subjectID, ArrayList<String> chapters,
            ArrayList<String> presentQuestionIDs, String classx) {
        int result = -1;
        ArrayList<Integer> chapterIDs = new ArrayList<>();
        ArrayList<Integer> matchingQuestionIDs = new ArrayList<>();

        //get all chapter ids
        for (String s : chapters) {
            chapterIDs.add(findChapterID(s, subjectID));
        }

        //get all matching questions...      
        for (int i : chapterIDs) {
            matchingQuestionIDs.addAll(getQuestionIDs(qType, i, classx, subjectID));
        }

        //remove present questions...
        for (int i = 0; i < matchingQuestionIDs.size(); i++) {
            int newID = matchingQuestionIDs.get(i);
            for (String oldID : presentQuestionIDs) {
                //if id already present...
                if (newID == Integer.parseInt(oldID)) {
                    matchingQuestionIDs.remove(i);
                    i--;
                    break;
                }
            }
        }

        //find one random question...
        if (matchingQuestionIDs.size() > 0) {
            int numOfResults = matchingQuestionIDs.size();
            int random = (int) (Math.random() * numOfResults);
            result = matchingQuestionIDs.get(random);
        }

        //return it
        return result;
    }

    /**
     * returns question id of a random question for given question types, from
     * given subject id in given chapters, that is not present in
     * presentQuestionIDs returns -1, if doesn't find any valid results, 2nd
     * version.. to handle the issue with duplicate question types in a subject
     */
    int getRandomQuestion2(String qType, int marks, int subjectID, ArrayList<String> chapters,
            ArrayList<String> presentQuestionIDs, String classx) {
        int result = -1;
        ArrayList<Integer> chapterIDs = new ArrayList<>();
        ArrayList<Integer> matchingQuestionIDs = new ArrayList<>();

        //find question type id..
        int qTypeID = findQuestionTypeID(qType, subjectID);

        //get all chapter ids
        for (String s : chapters) {
            chapterIDs.add(findChapterID(s, subjectID));
        }

        //get all matching questions...      
        for (int i : chapterIDs) {
            matchingQuestionIDs.addAll(getQuestionIDs(qType, i, marks, classx, subjectID));
        }

        //remove present questions...
        for (int i = 0; i < matchingQuestionIDs.size(); i++) {
            int newID = matchingQuestionIDs.get(i);
            for (String oldID : presentQuestionIDs) {
                //if id already present...
                if (newID == Integer.parseInt(oldID)) {
                    matchingQuestionIDs.remove(i);
                    i--;
                    break;
                }
            }
        }

        //find one random question...
        if (matchingQuestionIDs.size() > 0) {
            int numOfResults = matchingQuestionIDs.size();
            int random = (int) (Math.random() * numOfResults);
            result = matchingQuestionIDs.get(random);
        }

        //return it
        return result;
    }

    public String getInfoByPatternID(int id) {
        System.out.println("LOG::: Finding Pattern Info for pattern id(" + id + ")");
        try {
            PreparedStatement p = c.prepareStatement("SELECT INFORMATION FROM PAPERPATTERNS WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("INFORMATION");
                return result;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    public String getFixedByPatternID(int id) {
        System.out.println("LOG::: Finding Pattern Fixed Vaue for pattern id(" + id + ")");
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM PAPERPATTERNS WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("FIXED");
                return result;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    /**
     * Get Question type name value by Question ID
     *
     * @param questionID
     * @return
     */
    public String getQuestionTypeByQuestionID(int questionID, String classx, String subject) {
        System.out.println("LOG::: Finding Question type name value for question id(" + questionID + ")");
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(subject)
                    + " WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, questionID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("TYPE");
                return result;
            }
            throw new Exception("Type Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns all available questions
    ArrayList<Integer> getAllNotAddedQuestionsIDs(String qType, int subID,
            ArrayList<String> chapters, ArrayList<String> presentQuestionIDs, String classx) {

        ArrayList<Integer> chapterIDs = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();

        //get all chapter ids
        for (String s : chapters) {
            chapterIDs.add(findChapterID(s, subID));
        }

        //get all matching questions...      
        for (int i : chapterIDs) {
            result.addAll(getQuestionIDs(qType, i, classx, subID));
        }

        //remove present questions...
        for (int i = 0; i < result.size(); i++) {
            int newID = result.get(i);
            for (String oldID : presentQuestionIDs) {
                //if id already present...
                if (newID == Integer.parseInt(oldID)) {
                    result.remove(i);
                    i--;
                    break;
                }
            }
        }

        //return all
        return result;

    }

    //returns a list of ids for given subject...
    //returns empty list if none found...
    public ArrayList<String> getQuestionTypeIDsForSubject(int subID) {
        System.out.println("LOG::: Finding Question Type IDs for subject ID (" + subID + ")");
        ArrayList<String> result = new ArrayList<>();
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM QUESTIONTYPES WHERE "
                    + "UPPER(SUBJECT)  =  UPPER(?);");
            p.setInt(1, subID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                result.add("" + rs.getInt("ID"));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        }
    }

    //returns a list of question type names for given ids...
    //returns empty list if none found...
    public ArrayList<String> getQuestionTypeNamesByIDs(ArrayList<String> qTypeIDs) {
        System.out.println("LOG::: Finding Question Type Names for given IDs");
        ArrayList<String> result = new ArrayList<>();
        try {
            String statement = "SELECT * FROM QUESTIONTYPES WHERE ";
            for (int i = 0; i < qTypeIDs.size(); i++) {
                statement = statement + "UPPER(ID)  =  UPPER(?) ";
                if (i != (qTypeIDs.size() - 1)) {
                    statement = statement + "OR ";
                }
            }
            statement = statement + ";";
            System.out.println("TODO: DELETE ME: " + statement);
            PreparedStatement p = c.prepareStatement(statement);
            int i = 1;
            for (String id : qTypeIDs) {
                p.setInt(i++, Integer.parseInt(id));
            }
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("NAME"));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        }
    }

    //returns a list of question type marks for given ids...
    //returns empty list if none found...
    public ArrayList<String> getQuestionTypeMarksByIDs(ArrayList<String> qTypeIDs) {
        System.out.println("LOG::: Finding Question Type Marks for given IDs");
        ArrayList<String> result = new ArrayList<>();
        try {
            String statement = "SELECT * FROM QUESTIONTYPES WHERE ";
            for (int i = 0; i < qTypeIDs.size(); i++) {
                statement = statement + "UPPER(ID)  =  UPPER(?) ";
                if (i != (qTypeIDs.size() - 1)) {
                    statement = statement + "OR ";
                }
            }
            statement = statement + ";";
            System.out.println("TODO: DELETE ME: " + statement);
            PreparedStatement p = c.prepareStatement(statement);
            int i = 1;
            for (String id : qTypeIDs) {
                p.setInt(i++, Integer.parseInt(id));
            }
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                result.add("" + rs.getInt("MARKS"));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        }
    }

    /**
     * returns list of number of available questions.. against given qTypes
     * list..<br>
     * checks in provided chapters, subject
     *
     * @param idList question type ids list
     * @param chapterIDs list of chapter names
     * @param subject subject id
     * @return
     */
    public ArrayList<String> getListOfNumberofAvailableQuestionsByQTypeIDs(ArrayList<String> idList,
            ArrayList<String> chapters, String subjectID, String classx) {

        for (String c : chapters) {
            System.out.println("ASDF: " + c);
        }

        System.out.println("LOG::: Finding Number of available question");
        ArrayList<String> chapterIDs = getChapterIDsByNames(chapters, subjectID);
        ArrayList<String> typeList = DBManager.getInstance().getQuestionTypeNamesByIDs(idList);
        ArrayList<String> result = new ArrayList<>();

        if (chapterIDs.isEmpty() || idList.isEmpty()) {
            return result;
        }
        try {
            String statement = "SELECT TYPE "
                    + "FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(getSubjectName(Integer.parseInt(subjectID)))
                    + " WHERE ";
            for (int i = 0; i < chapterIDs.size(); i++) {
                statement = statement + "UPPER(CHAPTER)  =  UPPER(?) ";
                if (i != (chapterIDs.size() - 1)) {
                    statement = statement + "OR ";
                }
            }
            statement = statement + ";";
            System.out.println("TODO: DELETE ME: " + statement);
            PreparedStatement p = c.prepareStatement(statement);
            int x = 1;
            for (String c : chapterIDs) {
                //System.out.println(chapterIDs.size() + " ============== " + chapters.size());
                //System.out.println(c);
                //System.out.println(chapters.get(x - 1));
                p.setInt(x++, Integer.parseInt(c));
            }
            ResultSet rs = p.executeQuery();

            HashMap<String, String> idCount = new HashMap<>();
            //create an empty sting against each id...
            for (String id : typeList) {
                //System.out.println("TERI MERI" + id);
                idCount.put(id, "");
            }
            while (rs.next()) {
                //add a character to string for each id... hence increasing total count
                //System.out.println("" + rs.getString("TYPE") + " ========= " + (idCount.get("" + rs.getString("TYPE")) + "a"));
                idCount.put("" + rs.getString("TYPE"), (idCount.get("" + rs.getString("TYPE")) + "a"));
            }
            for (int i = 0; i < typeList.size(); i++) {
                result.add(idCount.get(typeList.get(i)).length() + "");
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return result;
        }
    }

    private ArrayList<String> getChapterIDsByNames(ArrayList<String> chapters, String subjectID) {
        ArrayList<String> result = new ArrayList<>();
        if (chapters.isEmpty()) {
            return result;
        }
        try {
            String statement = "SELECT * FROM CHAPTER WHERE ";
            for (int i = 0; i < chapters.size(); i++) {
                statement = statement + "UPPER(NAME)  =  UPPER(?) ";
                if (i != (chapters.size() - 1)) {
                    statement = statement + "OR ";
                }
            }
            statement = statement + ";";
            System.out.println("TODO: DELETE ME: " + statement);
            PreparedStatement p = c.prepareStatement(statement);
            int i = 1;
            for (String c : chapters) {
                p.setString(i++, c);
            }
            ResultSet rs = p.executeQuery();

            Hashtable<String, String> values = new Hashtable<>();
            while (rs.next()) {
                if (rs.getString("SUBJECT").equalsIgnoreCase(subjectID)) {
                    values.put(rs.getString("NAME"), rs.getString("ID"));
                }
            }
            for (int x = 0; x < chapters.size(); x++) {
                result.add(values.get(chapters.get(x)));
            }
            return result;
            //throw new Exception("Question ID Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //returns total marks of a pattern..
    String getTotalMarks(int patternID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM PAPERPATTERNS WHERE "
                    + "UPPER(ID)  =  UPPER(?) "
                    + "LIMIT 1;");
            p.setInt(1, patternID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String result = rs.getString("TOTALMARKS");
                return result;
            }
            throw new Exception("Pattern Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    private String filterClassSubjectName(String s) {
        return s.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll("-", "").replaceAll(" ", "").replaceAll("&", "");
    }

    //Inserts a new question in database...
    public void insertNewQuestion(String question, String answer, String qType,
            String chapter, String subject, String classx) {
        System.out.println("LOG::: Adding Custom Question");
        int chapterID = -1;
        try {
            int subjectID = findSubjectID(subject, classx, "default");
            chapterID = findChapterID(chapter, subjectID);
        } catch (Exception ex) {
            Logger.getLogger(papersystem.helper.DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        PreparedStatement p;
        int id = 0;
        try {
            p = c.prepareStatement("SELECT MAX(ID) AS HIGHESTID FROM QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(subject)
                    + " LIMIT 1;");
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                id = rs.getInt("HIGHESTID");
            } else {
                throw new Exception("ERROR getting highest ID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        try {
            p = c.prepareStatement(""
                    + "INSERT INTO QUESTIONS"
                    + filterClassSubjectName(classx) + filterClassSubjectName(subject)
                    + " (ID,QUESTION,ANSWER,TYPE,CHAPTER) "
                    + "VALUES (?, ?, ?, ?, ?);");
            p.setInt(1, id + 1);
            p.setString(2, question);
            p.setString(3, answer);
            p.setString(4, qType);
            p.setInt(5, chapterID);
            p.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //inserts a model paper details in database...
    void insertModelPaper(String name, String date, String questionPaperName,
            String answerPaperName, int subjectID) {
        System.out.println("LOG::: Adding Saved Paper Details");
        PreparedStatement p;
        int id = 0;
        try {
            p = c.prepareStatement("SELECT MAX(ID) AS HIGHESTID FROM MODELPAPERS "
                    + " LIMIT 1;");
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                id = rs.getInt("HIGHESTID");
            } else {
                id = 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        try {
            p = c.prepareStatement(""
                    + "INSERT INTO MODELPAPERS (ID, NAME, DATE, QFILE, AFILE, SUBJECT) "
                    + "VALUES (?, ?, ?, ?, ?,?);");
            p.setInt(1, id + 1);
            p.setString(2, name);
            p.setString(3, date);
            p.setString(4, questionPaperName);
            p.setString(5, answerPaperName);
            p.setInt(6, subjectID);
            p.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Inserted saved Paper Record by id: " + id + 1);
    }

    //returns a list of hashmaps containing all values of savedPaper for given subjectID
    public ArrayList<HashMap<String, String>> getSavedPapersForSubject(int subjectID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT * FROM MODELPAPERS WHERE "
                    + "UPPER(SUBJECT)  =  UPPER(?)");
            p.setInt(1, subjectID);
            ResultSet rs = p.executeQuery();

            ArrayList<HashMap<String, String>> result = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> record = new HashMap<>();
                record.put("ID", rs.getString("ID"));
                record.put("NAME", rs.getString("NAME"));
                record.put("DATE", rs.getString("DATE"));
                record.put("QFILE", rs.getString("QFILE"));
                record.put("AFILE", rs.getString("AFILE"));
                record.put("SUBJECT", rs.getString("SUBJECT"));

                result.add(record);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    //deletes a saved paper row from database based on ID
    public void deleteSavedPaper(int id) {

        try {
            PreparedStatement p = c.prepareStatement("DELETE FROM MODELPAPERS WHERE "
                    + "UPPER(ID)  =  UPPER(?)");
            p.setInt(1, id);
            p.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    //returns subject name against subject id
    private String getSubjectName(int subID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT NAME FROM SUBJECT WHERE "
                    + "UPPER(ID)  =  UPPER(?);");
            p.setInt(1, subID);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                return rs.getString("NAME");
            }
            throw new Exception("Subject Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    String getSubjectByQTypeID(int qTypeID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT SUBJECT FROM QUESTIONTYPES WHERE "
                    + "UPPER(ID)  =  UPPER(?);");
            p.setInt(1, qTypeID);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                int subID = rs.getInt("SUBJECT");
                return getSubjectName(subID);
            }
            throw new Exception("Subject Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    String getClassByQTypeID(int qTypeID) {
        try {
            PreparedStatement p = c.prepareStatement("SELECT SUBJECT FROM QUESTIONTYPES WHERE "
                    + "UPPER(ID)  =  UPPER(?);");
            p.setInt(1, qTypeID);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                int subID = rs.getInt("SUBJECT");
                p = c.prepareStatement("SELECT CLASS FROM SUBJECT WHERE "
                        + "UPPER(ID)  =  UPPER(?);");
                p.setInt(1, subID);
                rs = p.executeQuery();
                if (rs.next()) {
                    return rs.getString("CLASS");
                }
            }
            throw new Exception("Class Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return null;
        }
    }

    public int insertTemporaryPattern(String typesValue, String toAsk, String toAns, String marks,
            int subID, String info, String fixed, int totalMarks) {
        //first delete all temporary paper patterns
        try {
            PreparedStatement p = c.prepareStatement("DELETE FROM PAPERPATTERNS WHERE "
                    + "UPPER(NAME)  =  UPPER(?);");
            p.setString(1, "temporarypattern");
            p.executeUpdate();

            //nexxt find max paper pattern id            
            p = c.prepareStatement("SELECT MAX(ID) AS HIGHESTID FROM PAPERPATTERNS"
                    + " LIMIT 1;");
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("HIGHESTID");

                //next add paper pattern
                p = c.prepareStatement(""
                        + "INSERT INTO PAPERPATTERNS (ID, NAME, TYPES, QTOASK, QTOANS, MARKS, SUBJECT, INFORMATION, FIXED, TOTALMARKS) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                p.setInt(1, ++id);
                p.setString(2, "temporarypattern");
                p.setString(3, typesValue);
                p.setString(4, toAsk);
                p.setString(5, toAns);
                p.setString(6, marks);
                p.setInt(7, subID);
                p.setString(8, info);
                p.setString(9, fixed);
                p.setString(10, totalMarks + "");
                p.execute();

                //return patternID of added pattern
                return id;
            } else {
                throw new Exception("ERROR getting highest ID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return -1;
        }
    }

    public void tempMethodTestAllQTypeValuesInQuestions() {
        System.out.println("\n\n\nTesting Question Types");
        try {
            for (String classx : getClasses(null)) {
                for (String s : getSubjects(null, classx)) {
                    PreparedStatement p = c.prepareStatement("SELECT TYPE FROM QUESTIONS" + filterClassSubjectName(classx) + filterClassSubjectName(s));
                    ResultSet rs = p.executeQuery();

                    Set<String> typesInQT = new HashSet<String>();
                    while (rs.next()) {
                        String type = rs.getString("TYPE");
                        typesInQT.add(type);
                    }

                    p = c.prepareStatement("SELECT NAME FROM QUESTIONTYPES");
                    rs = p.executeQuery();
                    Set<String> typesInQTT = new HashSet<String>();
                    while (rs.next()) {
                        String type = rs.getString("NAME");
                        typesInQTT.add(type);
                    }

                    for (String x : typesInQT) {
                        boolean matchFound = false;
                        for (String y : typesInQTT) {
                            if (x.equals(y)) {
                                matchFound = true;
                                break;
                            }
                        }
                        if (!matchFound) {
                            System.out.println("ERROR:: TYPE NAME::: " + x);
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return;
        } catch (Exception ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            return;
        }
    }

    public void tempMethodTestAllChaptersInQuestionsFile() {
        Set<String> chaptersInDB = new HashSet<String>();
        Set<String> chaptersNotFound = new HashSet<String>();
        ArrayList<String> chaptersCorrectName = new ArrayList<>();
        try {
            PreparedStatement p = c.prepareStatement("SELECT NAME FROM CHAPTER");
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                String type = rs.getString("NAME");
                chaptersInDB.add(type);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("Questions List Final.xls"), ws);
            Sheet sheet = workbook.getSheet(0);

            int id = 0;
            int totalRows = sheet.getRows();
            //Skips first line...
            for (int i = 1; i < totalRows; i++) {
                Cell questionCell = sheet.getCell(0, i);
                //incase of empty row skip                 
                String question = questionCell.getContents().trim();
                if (question.length() == 0) {
                    continue;
                }
                Cell qTypeCell = sheet.getCell(2, i);
                String qType = qTypeCell.getContents().trim();
                Cell chapterCell = sheet.getCell(3, i);
                String chapter = chapterCell.getContents().trim().replace(".docx", "");
                if (chapter.length() == 0) {
                    continue;
                }

                boolean matchFound = false;
                for (String y : chaptersInDB) {
                    if (chapter.equalsIgnoreCase(y)) {
                        matchFound = true;
                        break;
                    }
                }

                if (!matchFound) {
                    chaptersNotFound.add(chapter);                    
                }
            }
            for (String y : chaptersNotFound) {
                ArrayList<Integer> matchValue = new ArrayList<>();
                for (String z : chaptersInDB) {
                    int i = StringUtils.getLevenshteinDistance(y, z);
                    matchValue.add(i);
                }
                int min = 0;
                for (int a = 0; a < matchValue.size(); a++) {
                    if (matchValue.get(min) > matchValue.get(a)) {
                        min = a;
                    }
                }
                chaptersCorrectName.add(chaptersInDB.toArray()[min].toString());
                System.out.println(y);
                System.out.println(y + "\t\t\t===\t\t\t" + chaptersInDB.toArray()[min]);
            }
            tempCorrectChaptersFile(chaptersNotFound.toArray(), chaptersCorrectName.toArray());
            workbook.close();

        } catch (IOException ex) {
            Logger.getLogger(papersystem.helper.DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(papersystem.helper.DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tempCorrectChaptersFile(Object[] wrongChapters, Object[] correctChapters) {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(new File("Questions List Final.xls"), ws);
            WritableWorkbook copy = Workbook.createWorkbook(new File("temp.xls"), workbook);
            Sheet sheet = workbook.getSheet(0);
            WritableSheet sheet2 = copy.getSheet(0);

            int id = 0;
            int totalRows = sheet.getRows();
            //Skips first line...
            for (int i = 1; i < totalRows; i++) {
                Cell questionCell = sheet.getCell(0, i);
                //incase of empty row skip
                Cell chapterCell = sheet.getCell(3, i);
                String chapter = chapterCell.getContents().trim().replace(".docx", "");
                if (chapter.length() == 0) {
                    continue;
                }

                System.out.println(wrongChapters.length + " aaaaaaaaaaaaaaaaaaaa "+ correctChapters.length);
                for (int j = 0; j < wrongChapters.length; j++) {
                    if (chapter.equals(wrongChapters[j].toString())) {
                        WritableCell cell = sheet2.getWritableCell(3, i);
                        Label l = (Label) cell;
                        l.setString(correctChapters[j].toString());
                    }
                }

            }
            copy.write();
            copy.close();
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
