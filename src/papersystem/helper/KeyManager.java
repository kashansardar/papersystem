/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Abbasi
 */
public class KeyManager {

    private static String secretKey = papersystem.PaperSystem.secretKey;

    public static void main(String[] args) throws SocketException, IOException {
    }

    public static String getID() {
        //check file exists...
        File file = new File(System.getenv("LOCALAPPDATA") + "//"+papersystem.PaperSystem.dataFolderName+"//kpsk.dat");
        //if file not present, creaeate new key
        if (!file.exists()) {
            String key = "";
            for (int i = 0; i < 8; i++) {
                key = key + RandomChar();
            }

            try {
                Files.write(Paths.get(System.getenv("LOCALAPPDATA") + "//"+papersystem.PaperSystem.dataFolderName+"//kpsk.dat"),
                        key.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error! Open program using Admin Rights.");
            }
            return key;
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
            if (key.length() == 8) {
                return key;
            } else {
                JOptionPane.showMessageDialog(null, "Error! ID Corrupted. Contact support for new Serial Key.");
                String x = "";
                for (int i = 0; i < 8; i++) {
                    x = x + RandomChar();
                }
                return x;
            }
        }

    }

    private static char RandomChar() {
        int x = 58;
        x = (int) (Math.random() * 25) + 65;

        return (char) x;
    }

    public static boolean isKeyValid(String key) {
        //format key..
        key = key.replace("-", "");
        key = key.replace(" ", "");

        if (key.length() != 12) {
            return false;
        }

        //Regenerating the key and date
        int month = key.charAt(10) - 'd';
        int year = key.charAt(11) - 'e' + 2015;
        String keyString = secretKey + key.charAt(10) + key.charAt(11);

        //formatting mac address...
        String msg = getID();
        msg = msg.toUpperCase();
        if (msg == null) {
            return false;
        }
        msg = msg.replace(":", "");
        msg = msg.replace(" ", "");

        //Each time a new date is entered, a new key is generated...
        //When decoding, last two characters are taken to generate the key...
        String algo = "HmacMD5";
        String digest = null;
        try {
            SecretKeySpec keys = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = null;
            try {
                mac = Mac.getInstance(algo);
                mac.init(keys);
            } catch (Exception ex) {
            }

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        }
        digest = digest.substring(0, 10);

        return (digest.compareToIgnoreCase(key.substring(0, 10)) == 0);

    }

    public static boolean isKeyExpired(String key) {
        //format key..
        key = key.replace("-", "");
        key = key.replace(" ", "");

        if (key.length() != 12) {
            return true;
        }

        //Regenerating the key and date
        int month = key.charAt(10) - 'd';
        int year = key.charAt(11) - 'e' + 2015;

        System.out.println(year);
        System.out.println(month);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        int cYear = Integer.parseInt(dateFormat.format(date).substring(0, 4));
        int cMonth = Integer.parseInt(dateFormat.format(date).substring(5, 7));

        if (cYear < year) {
            return false;
        }
        if (cYear == year) {
            if (cMonth < month) {
                return false;
            }
        }

        return true;
    }
}
