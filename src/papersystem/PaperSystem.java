/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import papersystem.helper.DBManager;
import papersystem.helper.KeyManager;

/**
 *
 * @author Abbasi
 */
public class PaperSystem {

    public static String softwareName = "KIRAN Paper Setter";
    public static String code;
    public static String secretKey;
    public static String dataFolderName;

    public static void main(String[] args) {
        setCodeVariables();
        //Delete previous temp files... temp folder holds word files created to show document
        deletePrevFiles(new File(System.getenv("LOCALAPPDATA") + "//" + dataFolderName + "//temp"));
        //Make new Temp Directory
        new File(System.getenv("LOCALAPPDATA") + "//" + dataFolderName).mkdir();
        //Check all Files are present...  

        boolean activated = checkApplicationActivated();
//        activated = true;

        if (activated) {
            startProgram();
        } else {
            new registrationlFrame().main(null);
        }
    }

    public static void deletePrevFiles(File file) {
        if (file.isDirectory()) {
            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());
            } else {
                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    deletePrevFiles(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public static String getMonthText(int i) {
        switch (i) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return null;
        }
    }

    private static boolean checkApplicationActivated() {
        //check file exists...
        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        System.out.println(fw.getDefaultDirectory());
        File file = new File(System.getenv("LOCALAPPDATA") + "//" + dataFolderName + "//kpsk.cfg");
        //if file not present, activated = false
        if (!file.exists()) {
            return false;
        } //if file present, get key and check key...
        else {
            BufferedReader reader = null;
            String key = null;

            try {
                reader = new BufferedReader(new FileReader(file));

                key = reader.readLine();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                }
            }
            System.out.println(key);

            //if key valid, time correct, activated = true,
            if (KeyManager.isKeyValid(key) && !KeyManager.isKeyExpired(key)) {
                return true;
            } else if (KeyManager.isKeyValid(key) && KeyManager.isKeyExpired(key)) {
                JOptionPane.showMessageDialog(null, "Your Key has expired.");
                return false;
            } //else delete file, activated = false;
            else {
                file.delete();
                return false;
            }
        }
    }

    static void startProgram() {
        // Starting DB..
        DBManager.getInstance();
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\nTesting Chapters\n-----------------------------------------");
//        DBManager.getInstance().tempMethodTestAllChaptersInQuestionsFile();
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\nTesting QTypes\n-----------------------------------------");
//        DBManager.getInstance().tempMethodTestAllQTypeValuesInQuestions();

        MainScreen.main(null);
    }

    private static void setCodeVariables() {
        ArrayList<String> classes = DBManager.getInstance().getClasses(null);
        if (classes.size() == 1) {
            String c = classes.get(0);
            if (c.equals("8th (English)")) {
                //8th English
                code = "8en";
            } else if (c.equals("9th (English)")) {
                //9th English
                code = "9sc";
            } else if (c.equals("10th (English)")) {
                //10th English
                code = "10sc";
            } else if (c.equals("11th Commerce")) {
                //11th Commerce
                code = "11com";
            } else if (c.equals("12th Commerce")) {
                //12th Commerce
                code = "12com";
            } else if (c.equals("11th Science")) {
                //11th Science
                code = "Only11sc";
            } else if (c.equals("12th Science")) {
                //12th Science
                code = "Only12sc";
            }
            secretKey = "MySecretKeyPaperSetter" + code;
            dataFolderName = "kpsprogramFinal" + code;
        }
    }
}
