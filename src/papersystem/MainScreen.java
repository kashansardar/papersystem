/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author Abbasi
 */
public class MainScreen extends javax.swing.JFrame {

    /**
     * Creates new form MainScreen
     */
    JFrame thisClass = this;
    public static MainScreen mainFrame;
    public static JFrame loading;

    public MainScreen() {
        this.setContentPane(new JLabel(new ImageIcon(getClass().getResource(("bg.jpg")))));
        initComponents();

        //Draw Logo
        //LogoPanel.setLayout(new BorderLayout(10, 10));
        //LogoPanel.add(new JLabel(new ImageIcon(getClass().getResource(("logo3.png")))), BorderLayout.CENTER);
        //setupLoader();
        bodyPanel.removeAll();
        JPanel board = new BoardPatternPanel();
        board.setSize(808, 600);
        bodyPanel.add(board);
        bodyPanel.validate();
        bodyPanel.repaint();

        //Code for handling left side bar buttons...
        boardPatternButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                resetSideBar();
                boardPatternButton.setBackground(new Color(249, 249, 249));

                //First remove any prev loaded components..
                bodyPanel.removeAll();
                JPanel board = new BoardPatternPanel();
                board.setSize(808, 600);
                bodyPanel.add(board);
                bodyPanel.revalidate();
                bodyPanel.repaint();

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        customPatternButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {                
                resetSideBar();
                customPatternButton.setBackground(new Color(249, 249, 249));

                //First remove any prev loaded components..
                bodyPanel.removeAll();
                JPanel custom = new CustomPatternPanel();
                custom.setSize(808, 600);
                bodyPanel.add(custom);
                bodyPanel.revalidate();
                bodyPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        fixedPatternButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                resetSideBar();
                fixedPatternButton.setBackground(new Color(249, 249, 249));

                //First remove any prev loaded components..
                bodyPanel.removeAll();
                JPanel fixed = new FixedPatternPanel();
                fixed.setSize(808, 600);
                bodyPanel.add(fixed);
                bodyPanel.revalidate();
                bodyPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        modelAnsButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                resetSideBar();
                questionEntryButton.setBackground(new Color(249, 249, 249));

                //First remove any prev loaded components..
                bodyPanel.removeAll();
                JPanel savedPaper = new savedPaperPanel();
                savedPaper.setSize(808, 600);
                bodyPanel.add(savedPaper);
                bodyPanel.validate();
                bodyPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        questionEntryButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                resetSideBar();
                questionEntryButton.setBackground(new Color(249, 249, 249));

                //First remove any prev loaded components..
                bodyPanel.removeAll();
                JPanel qEntry = new QuestionEntryPanel();
                qEntry.setSize(808, 600);
                bodyPanel.add(qEntry);
                bodyPanel.validate();
                bodyPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        prevPapersButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                resetSideBar();
                prevPapersButton.setBackground(new Color(249, 249, 249));

                //First remove any prev loaded components..
                bodyPanel.removeAll();
                JPanel pPapers = new PrevPapersPanel();
                pPapers.setSize(808, 600);
                bodyPanel.add(pPapers);
                bodyPanel.validate();
                bodyPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        settingsButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (true) {
                    return;
                }
                resetSideBar();
                settingsButton.setBackground(new Color(249, 249, 249));

                //First remove any prev loaded components..
                bodyPanel.removeAll();
                JPanel settings = new SettingsPanel();
                settings.setSize(808, 600);
                bodyPanel.add(settings);
                bodyPanel.validate();
                bodyPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sideBar = new javax.swing.JPanel();
        boardPatternButton = new javax.swing.JPanel();
        questionEntryButton = new javax.swing.JPanel();
        prevPapersButton = new javax.swing.JPanel();
        settingsButton = new javax.swing.JPanel();
        fixedPatternButton = new javax.swing.JPanel();
        modelAnsButton = new javax.swing.JPanel();
        customPatternButton = new javax.swing.JPanel();
        bodyPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kiran Paper Setter");
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);

        sideBar.setOpaque(false);

        boardPatternButton.setBackground(new java.awt.Color(255, 0, 0));
        boardPatternButton.setOpaque(false);

        javax.swing.GroupLayout boardPatternButtonLayout = new javax.swing.GroupLayout(boardPatternButton);
        boardPatternButton.setLayout(boardPatternButtonLayout);
        boardPatternButtonLayout.setHorizontalGroup(
            boardPatternButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        boardPatternButtonLayout.setVerticalGroup(
            boardPatternButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        questionEntryButton.setBackground(new java.awt.Color(102, 102, 255));
        questionEntryButton.setOpaque(false);

        javax.swing.GroupLayout questionEntryButtonLayout = new javax.swing.GroupLayout(questionEntryButton);
        questionEntryButton.setLayout(questionEntryButtonLayout);
        questionEntryButtonLayout.setHorizontalGroup(
            questionEntryButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        questionEntryButtonLayout.setVerticalGroup(
            questionEntryButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        prevPapersButton.setBackground(new java.awt.Color(255, 255, 255));
        prevPapersButton.setOpaque(false);

        javax.swing.GroupLayout prevPapersButtonLayout = new javax.swing.GroupLayout(prevPapersButton);
        prevPapersButton.setLayout(prevPapersButtonLayout);
        prevPapersButtonLayout.setHorizontalGroup(
            prevPapersButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );
        prevPapersButtonLayout.setVerticalGroup(
            prevPapersButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 63, Short.MAX_VALUE)
        );

        settingsButton.setBackground(new java.awt.Color(255, 255, 102));
        settingsButton.setOpaque(false);

        javax.swing.GroupLayout settingsButtonLayout = new javax.swing.GroupLayout(settingsButton);
        settingsButton.setLayout(settingsButtonLayout);
        settingsButtonLayout.setHorizontalGroup(
            settingsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        settingsButtonLayout.setVerticalGroup(
            settingsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 63, Short.MAX_VALUE)
        );

        fixedPatternButton.setOpaque(false);

        javax.swing.GroupLayout fixedPatternButtonLayout = new javax.swing.GroupLayout(fixedPatternButton);
        fixedPatternButton.setLayout(fixedPatternButtonLayout);
        fixedPatternButtonLayout.setHorizontalGroup(
            fixedPatternButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        fixedPatternButtonLayout.setVerticalGroup(
            fixedPatternButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );

        modelAnsButton.setOpaque(false);

        javax.swing.GroupLayout modelAnsButtonLayout = new javax.swing.GroupLayout(modelAnsButton);
        modelAnsButton.setLayout(modelAnsButtonLayout);
        modelAnsButtonLayout.setHorizontalGroup(
            modelAnsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        modelAnsButtonLayout.setVerticalGroup(
            modelAnsButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );

        customPatternButton.setBackground(new java.awt.Color(102, 204, 0));
        customPatternButton.setOpaque(false);

        javax.swing.GroupLayout customPatternButtonLayout = new javax.swing.GroupLayout(customPatternButton);
        customPatternButton.setLayout(customPatternButtonLayout);
        customPatternButtonLayout.setHorizontalGroup(
            customPatternButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        customPatternButtonLayout.setVerticalGroup(
            customPatternButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout sideBarLayout = new javax.swing.GroupLayout(sideBar);
        sideBar.setLayout(sideBarLayout);
        sideBarLayout.setHorizontalGroup(
            sideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(boardPatternButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(questionEntryButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(fixedPatternButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(modelAnsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(customPatternButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(sideBarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(prevPapersButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        sideBarLayout.setVerticalGroup(
            sideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(boardPatternButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(fixedPatternButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(customPatternButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(modelAnsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(questionEntryButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(sideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(settingsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prevPapersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        bodyPanel.setBackground(new java.awt.Color(249, 249, 249));
        bodyPanel.setOpaque(false);

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 757, Short.MAX_VALUE)
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sideBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bodyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sideBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         //         */
        try {

            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainFrame = new MainScreen();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel boardPatternButton;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JPanel customPatternButton;
    private javax.swing.JPanel fixedPatternButton;
    private javax.swing.JPanel modelAnsButton;
    private javax.swing.JPanel prevPapersButton;
    private javax.swing.JPanel questionEntryButton;
    private javax.swing.JPanel settingsButton;
    private javax.swing.JPanel sideBar;
    // End of variables declaration//GEN-END:variables

    public void resetSideBar() {
        boardPatternButton.setBackground(new Color(240, 240, 240));
        customPatternButton.setBackground(new Color(240, 240, 240));
        fixedPatternButton.setBackground(new Color(240, 240, 240));
        questionEntryButton.setBackground(new Color(240, 240, 240));
        prevPapersButton.setBackground(new Color(240, 240, 240));
        settingsButton.setBackground(new Color(240, 240, 240));
    }

    static void setupLoader() {
        loading = new JFrame();
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("    Please wait..."), BorderLayout.CENTER);
        loading.setUndecorated(true);
        loading.getContentPane().add(p1);
        loading.setPreferredSize(new Dimension(120, 30));
        p1.setBackground(Color.white);

        loading.setLocationRelativeTo(mainFrame);
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        //loading.setModal(true);
        loading.pack();
        loading.setVisible(true);
        loading.setVisible(false);
    }

}
