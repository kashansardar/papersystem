/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import papersystem.helper.DBManager;
import papersystem.helper.PaperPrinter;

/**
 *
 * @author Abbasi
 */
public class FixedPatternPanel extends javax.swing.JPanel {

    DBManager myDBManager = DBManager.getInstance();
    JPanel panelforCBL = new JPanel(new FlowLayout(FlowLayout.LEFT));
    CheckBoxList chapters;    
//    FixedData fixedData = new FixedData();
    Border scrollPaneBorder = null;
    Component thisComponent = null;

    /**
     * Creates new form FixedPatternPanel
     */
    public FixedPatternPanel() {
        initComponents();
        scrollPane.getViewport().setOpaque(false);
        scrollPaneBorder = scrollPane.getBorder();
        thisComponent = this;
        
        
        bGenerateFixed.setIcon((new ImageIcon(getClass().getResource(("gen.png")))));
        bGenerateFixed.setBorderPainted(false);
        bGenerateFixed.setFocusPainted(false);
        bGenerateFixed.setContentAreaFilled(false);
        informationFixed.setEditable(false);
       
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        subjectComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateChapterCBL();
                    updatePatternComboBox();
                }
            }
        });
        classComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateSubjectComboBox();
                    updateChapterCBL();
                }
            }
        });
        patternComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    informationFixed.setText(DBManager.getInstance().getInfoByPatternID(
                            DBManager.getInstance().findPatternID(
                                    patternComboBox.getSelectedItem().toString().trim(),
                                    DBManager.getInstance().findSubjectID(
                                            subjectComboBox.getSelectedItem().toString(),
                                            classComboBox.getSelectedItem().toString(),
                                            "default"
                                    ))));
                }
            }
        });
        bGenerateFixed.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                PaperPrinter pp = new PaperPrinter();
                if (paperName.getText() == null || paperName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a Paper Name.");
                    return;
                }
                try {
                    System.out.println(dateFixed.getDate().toString());
                } catch (Exception e22) {
                    JOptionPane.showMessageDialog(null, "Please enter a Date.");
                    return;
                }
                if (timeFixed.getText() == null || timeFixed.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter time for Paper.");
                    return;
                }
                String date = (dateFixed.getDate().getDate()) + "-" + PaperSystem.getMonthText((dateFixed.getDate().getMonth() + 1)) + "-" + (dateFixed.getDate().getYear() + 1900);
                String time = timeFixed.getText() + " " + timeUnitsFixed.getSelectedItem().toString();
                String instructions = null;
//                String instructions = instructionsFixed.getText();
//                if (instructions.length() == 0) {
//                    instructions = null;
//                }
                String classx = classComboBox.getSelectedItem().toString();
                String subject = subjectComboBox.getSelectedItem().toString();
                int patternID = DBManager.getInstance().findPatternID(patternComboBox.getSelectedItem().toString(),
                        DBManager.getInstance().findSubjectID(subject, classx, null));
                pp.print(chapters.getSelected(), paperName.getText(),
                        date, time,
                        instructions,
                        classx,
                        subject,
                        patternID,
                        "default",
                        MainScreen.mainFrame);
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
        updateClassComboBox();
        updateSubjectComboBox();
        updateChapterCBL();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        classComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        subjectComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        paperName = new javax.swing.JTextField();
        scrollPane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        allChaptersRB = new javax.swing.JRadioButton();
        selectedChaptersRB = new javax.swing.JRadioButton();
        checkBoxListHolder = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        bGenerateFixed = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        informationFixed = new javax.swing.JTextArea();
        patternComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dateFixed = new org.jdesktop.swingx.JXDatePicker();
        jLabel7 = new javax.swing.JLabel();
        timeFixed = new javax.swing.JTextField();
        timeUnitsFixed = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(249, 249, 249));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1000, 600));

        classComboBox.setLightWeightPopupEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Subject:");

        subjectComboBox.setLightWeightPopupEnabled(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Paper Name:");

        paperName.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N

        scrollPane.setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(249, 249, 249));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(825, 380));

        allChaptersRB.setBackground(new java.awt.Color(249, 249, 249));
        allChaptersRB.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        allChaptersRB.setText("All Chapters");
        allChaptersRB.setOpaque(false);
        allChaptersRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allChaptersRBActionPerformed(evt);
            }
        });

        selectedChaptersRB.setBackground(new java.awt.Color(249, 249, 249));
        selectedChaptersRB.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectedChaptersRB.setText("Selected Chapters");
        selectedChaptersRB.setOpaque(false);
        selectedChaptersRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedChaptersRBActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(249, 249, 249));
        jPanel2.setOpaque(false);

        bGenerateFixed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bGenerateFixed.setText("Generate Paper");
        bGenerateFixed.setOpaque(false);
        bGenerateFixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGenerateFixedActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Instructions:");

        informationFixed.setColumns(20);
        informationFixed.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        informationFixed.setRows(5);
        jScrollPane2.setViewportView(informationFixed);

        patternComboBox.setLightWeightPopupEnabled(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Exam:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bGenerateFixed)))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(patternComboBox, 0, 318, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(patternComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addComponent(bGenerateFixed)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(checkBoxListHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(allChaptersRB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectedChaptersRB)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(allChaptersRB)
                            .addComponent(selectedChaptersRB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxListHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        scrollPane.setViewportView(jPanel1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Class:");
        jLabel1.setVerifyInputWhenFocusTarget(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Time:");

        timeFixed.setToolTipText("Enter Time");
        timeFixed.setName(""); // NOI18N
        timeFixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeFixedActionPerformed(evt);
            }
        });

        timeUnitsFixed.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mins", "Hrs" }));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Date:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(paperName))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(classComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(subjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateFixed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(timeFixed, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(timeUnitsFixed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7))))))
                .addContainerGap(260, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(classComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(subjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(timeFixed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(timeUnitsFixed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(dateFixed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(paperName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void allChaptersRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allChaptersRBActionPerformed
        if (allChaptersRB.isSelected()) {
            System.out.println("All Chapters");
            chapters.saveState();
            chapters.selectAll();
            chapters.disableEditing();
            selectedChaptersRB.setSelected(false);
        } else {
            allChaptersRB.setSelected(true);
        }
    }//GEN-LAST:event_allChaptersRBActionPerformed

    private void selectedChaptersRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedChaptersRBActionPerformed
        if (selectedChaptersRB.isSelected()) {
            System.out.println("Selected Chapters");
            chapters.loadState();
            chapters.enableEditing();
            allChaptersRB.setSelected(false);
        } else {
            selectedChaptersRB.setSelected(true);
        }
    }//GEN-LAST:event_selectedChaptersRBActionPerformed

    private void bGenerateFixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGenerateFixedActionPerformed

    }//GEN-LAST:event_bGenerateFixedActionPerformed

    private void timeFixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeFixedActionPerformed

    }//GEN-LAST:event_timeFixedActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton allChaptersRB;
    public javax.swing.JButton bGenerateFixed;
    private javax.swing.JScrollPane checkBoxListHolder;
    private javax.swing.JComboBox classComboBox;
    public org.jdesktop.swingx.JXDatePicker dateFixed;
    public javax.swing.JTextArea informationFixed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField paperName;
    private javax.swing.JComboBox patternComboBox;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JRadioButton selectedChaptersRB;
    private javax.swing.JComboBox subjectComboBox;
    public javax.swing.JTextField timeFixed;
    public javax.swing.JComboBox timeUnitsFixed;
    // End of variables declaration//GEN-END:variables

    private void updateClassComboBox() {
        classComboBox.removeAllItems();
        for (String s : myDBManager.getClasses(null)) {
            classComboBox.addItem(s);
        }
    }

    private void updateSubjectComboBox() {
        subjectComboBox.removeAllItems();
        for (String s : myDBManager.getSubjects(null, classComboBox.getSelectedItem().toString())) {
            subjectComboBox.addItem(s);
        }
        updateChapterCBL();
        updatePatternComboBox();
    }

    private void updatePatternComboBox() {
        patternComboBox.removeAllItems();
        for (String s : myDBManager.getPaperPatterns(myDBManager.findSubjectID(subjectComboBox.getSelectedItem().toString(), classComboBox.getSelectedItem().toString(), "default"))) {
            if (s.compareToIgnoreCase("Board Pattern") == 0) {
                continue;
            }
            patternComboBox.addItem(s);
        }
    }

    private void updateChapterCBL() {
        System.out.println(classComboBox.getSelectedItem().toString());
        System.out.println(subjectComboBox.getSelectedItem().toString());
        chapters = new CheckBoxList(1, 2, myDBManager.getChapters(null, classComboBox.getSelectedItem().toString(), subjectComboBox.getSelectedItem().toString()));

        selectedChaptersRB.setSelected(false);
        allChaptersRB.setSelected(true);
        chapters.disableEditing();
        panelforCBL.removeAll();
        panelforCBL.add(chapters);
        checkBoxListHolder.setViewportView(panelforCBL);
        checkBoxListHolder.getViewport().setBackground(Color.white);
        checkBoxListHolder.setBackground(Color.white);
    }
}
