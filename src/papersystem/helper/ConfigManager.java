/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Abbasi
 */
//A singleton class that will hold all the info like class.. subjects... question types etc....
public class ConfigManager {

    public static String board = null;
    public static ArrayList<String> qTypes = null;

    private static ConfigManager myInstance = null;

    public ConfigManager() {
        if (myInstance != null) {
            return;
        }

        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("config.txt"));
            //line 1: gets board from config File
            board = br.readLine();
            System.out.println("Board = " + board);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //TODO get Question Types from DB
        qTypes = new ArrayList<>();

        myInstance = this;
    }
}
