/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.UIManager;

/**
 *
 * @author Abbasi
 */
public class CheckBoxList extends JPanel implements Scrollable {

    ArrayList<Boolean> checkBoxStates = new ArrayList<>();
    ArrayList<Checkbox> myCheckBoxes = new ArrayList<>();
    ArrayList<JLabel> myLabels = new ArrayList<>();

    public CheckBoxList(int width, int height, ArrayList<String> items) {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        myCheckBoxes.clear();
        checkBoxStates.clear();
        for (int i = 0; i < items.size(); i++) {
            Checkbox box = new Checkbox("", true);
            JLabel label = new JLabel(items.get(i));
            label.setFont(new java.awt.Font("SansSerif", 0, 11));
            
            //box.setBackground(Color.white);
            myCheckBoxes.add(box);
            myLabels.add(label);
            checkBoxStates.add(new Boolean(false));
            c.anchor = GridBagConstraints.LINE_START;
            c.gridx = 0;
            c.gridy = i;
            this.add(myCheckBoxes.get(i), c);
            c.gridx = 1;
            this.add(label, c);
        }
        //this.setOpaque(false);
    }

    //Saves all the currently selected checkboxes
    public void saveState() {
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            checkBoxStates.set(i, myCheckBoxes.get(i).getState());
        }
    }

    //TODO...
    //Loads all the currently selected checkboxes
    public void loadState() {
        deselectAll();//delete me...
        saveState();//delete me...
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            myCheckBoxes.get(i).setState(checkBoxStates.get(i));
        }
    }

    //Enables all the checkboxes
    public void selectAll() {
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            myCheckBoxes.get(i).setState(true);            
        }
    }

    //Disables all the checkboxes
    public void deselectAll() {
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            myCheckBoxes.get(i).setState(false);            
        }
    }

    //returns data in selected checkboxes
    public ArrayList<String> getSelected() {
        ArrayList<String> selectedItems = new ArrayList<>();

        for (int i = 0; i < myCheckBoxes.size(); i++) {
            if (myCheckBoxes.get(i).getState() == true) {
                selectedItems.add(myLabels.get(i).getText().trim());
                System.out.println(myCheckBoxes.get(i).getLabel());
            }
        }
        return selectedItems;
    }

    //Disables editing of checkboxes...
    public void disableEditing() {
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            myCheckBoxes.get(i).setEnabled(false);
            myLabels.get(i).setEnabled(false);
        }
    }

    //Enables editing of checkboxes...
    public void enableEditing() {
        for (int i = 0; i < myCheckBoxes.size(); i++) {
            myCheckBoxes.get(i).setEnabled(true);
            myLabels.get(i).setEnabled(true);
        }
    }

    public static void main(String args[]) {
        JFrame x = new JFrame();
        try {

            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        ArrayList<String> aaa = new ArrayList<>();
        aaa.add("12zxda3");
        aaa.add("12azxc3");
        aaa.add("12avvg3");
        aaa.add("1a2bb3");
        aaa.add("a123");

        x.add(new CheckBoxList(1, 2, aaa));
        x.setVisible(true);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return super.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
