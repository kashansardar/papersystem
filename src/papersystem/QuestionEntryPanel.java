/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papersystem;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import papersystem.helper.DBManager;

/**
 *
 * @author Abbasi
 */
public class QuestionEntryPanel extends javax.swing.JPanel {

    /**
     * Creates new form QuestionEntryPanel
     */
    DBManager myDBManager = DBManager.getInstance();

    //holds the data about images
    File questionImage = null;
    File answerImage = null;

    public QuestionEntryPanel() {
        initComponents();

        updateClassComboBox();
        updateSubjectComboBox();
        updateChapterComboBox();
        updateQTypeComboBox();
        
        qTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                updateMarks();
            }}
        });
        
        subjectComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateChapterComboBox();
                    updateQTypeComboBox();
                }
            }
        });
        classComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                updateSubjectComboBox();
                updateChapterComboBox();
                updateQTypeComboBox();
            }
        });

        updateMarks();
        aImageButton.setEnabled(false);
        qImageButton.setEnabled(false);
        answerTextArea.setEnabled(false);
        aImageCheckBox.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        classComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        subjectComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        chapterComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        qTypeComboBox = new javax.swing.JComboBox();
        insertButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        questionTextArea = new javax.swing.JTextArea();
        answerCheckBox = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        answerTextArea = new javax.swing.JTextArea();
        aImageCheckBox = new javax.swing.JCheckBox();
        aImageButton = new javax.swing.JButton();
        qImageCheckBox = new javax.swing.JCheckBox();
        qImageButton = new javax.swing.JButton();
        marksLabel = new javax.swing.JLabel();

        setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Class:");

        classComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Subject:");

        subjectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Chapter:");

        chapterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Question Type:");

        qTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        insertButton.setText("Insert");
        insertButton.setOpaque(false);
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Question");

        questionTextArea.setColumns(20);
        questionTextArea.setRows(5);
        jScrollPane2.setViewportView(questionTextArea);

        answerCheckBox.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        answerCheckBox.setText("Answer:");
        answerCheckBox.setOpaque(false);
        answerCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                answerCheckBoxActionPerformed(evt);
            }
        });

        answerTextArea.setColumns(20);
        answerTextArea.setRows(5);
        jScrollPane3.setViewportView(answerTextArea);

        aImageCheckBox.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        aImageCheckBox.setText("Answer Image");
        aImageCheckBox.setOpaque(false);
        aImageCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aImageCheckBoxActionPerformed(evt);
            }
        });

        aImageButton.setText("Browse");
        aImageButton.setOpaque(false);
        aImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aImageButtonActionPerformed(evt);
            }
        });

        qImageCheckBox.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        qImageCheckBox.setText("Question Image");
        qImageCheckBox.setOpaque(false);
        qImageCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qImageCheckBoxActionPerformed(evt);
            }
        });

        qImageButton.setText("Browse");
        qImageButton.setOpaque(false);
        qImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qImageButtonActionPerformed(evt);
            }
        });

        marksLabel.setText("88 Marks");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(qImageCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(qImageButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(aImageCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(aImageButton))
                    .addComponent(answerCheckBox)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(insertButton)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(classComboBox, 0, 274, Short.MAX_VALUE)
                                    .addComponent(chapterComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(55, 55, 55)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(subjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(qTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(marksLabel))))
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3))
                .addContainerGap(82, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(chapterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(qTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(marksLabel))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qImageCheckBox)
                    .addComponent(qImageButton))
                .addGap(22, 22, 22)
                .addComponent(answerCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aImageCheckBox)
                    .addComponent(aImageButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(insertButton)
                .addContainerGap(61, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void aImageCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aImageCheckBoxActionPerformed
        if (aImageCheckBox.isSelected()) {
            aImageButton.setEnabled(true);
        } else {
            aImageButton.setEnabled(false);
        }
    }//GEN-LAST:event_aImageCheckBoxActionPerformed

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        String question = questionTextArea.getText().trim();
        String answer = "void";

        if (question == null || question.isEmpty()) {
            JOptionPane.showMessageDialog(MainScreen.mainFrame, "Please enter Question");
            return;
        }
        if (qImageCheckBox.isSelected()) {
            //check image is selected...
            if (questionImage == null) {
                JOptionPane.showMessageDialog(MainScreen.mainFrame, "Please select a Question Image.");
                return;
            }
            //copy the image file in correct folder.. with unique name            
            String path = "images//"
                    + classComboBox.getSelectedItem().toString().trim()
                    + "//" + subjectComboBox.getSelectedItem().toString().trim()
                    + "//";
            String filename = "custom" + (new File(path).listFiles().length + 1);
            File dest = new File(path + filename);

            try {
                copyFileUsingChannel(questionImage, dest);
            } catch (IOException ex) {
                Logger.getLogger(QuestionEntryPanel.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Unable to copy Question Image File.");
                JOptionPane.showMessageDialog(MainScreen.mainFrame, "Unable to save Question Image");
                return;
            }
            //update question to add IMG() line
            question = question + "\n" + "IMG(" + dest.getName() + ")";
        }
        if (answerCheckBox.isSelected()) {
            answer = answerTextArea.getText().trim();
            if (answer == null || answer.isEmpty()) {
                JOptionPane.showMessageDialog(MainScreen.mainFrame, "Please enter Answer");
                return;
            }
            if (aImageCheckBox.isSelected()) {//check image is selected...
                if (answerImage == null) {
                    JOptionPane.showMessageDialog(MainScreen.mainFrame, "Please select an Answer Image.");
                    return;
                }
                //copy the image file in correct folder.. with unique name            
                String path = "images//"
                        + classComboBox.getSelectedItem().toString().trim()
                        + "//" + subjectComboBox.getSelectedItem().toString().trim()
                        + "//";
                String filename = "custom" + (new File(path).listFiles().length + 1);
                File dest = new File(path + filename);

                try {
                    copyFileUsingChannel(answerImage, dest);
                } catch (IOException ex) {
                    Logger.getLogger(QuestionEntryPanel.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Unable to copy Question Image File.");
                    JOptionPane.showMessageDialog(MainScreen.mainFrame, "Unable to save Answer Image");
                    return;
                }
                //update question to add IMG() line
                answer = answer + "\n" + "IMG(" + dest.getName() + ")";
            }
        }
        DBManager.getInstance().insertNewQuestion(question, answer,
                qTypeComboBox.getSelectedItem().toString(),
                chapterComboBox.getSelectedItem().toString(),
                subjectComboBox.getSelectedItem().toString(),
                classComboBox.getSelectedItem().toString());
        JOptionPane.showMessageDialog(MainScreen.mainFrame, "Question has been added.");
        questionImage = null;
        answerImage = null;
        aImageButton.setEnabled(false);
        qImageButton.setEnabled(false);
        answerTextArea.setEnabled(false);
        aImageCheckBox.setEnabled(false);
        answerCheckBox.setSelected(false);
        aImageCheckBox.setSelected(false);
        qImageCheckBox.setSelected(false);
        qImageCheckBox.setText("Question Image: ");
        aImageCheckBox.setText("Answer Image: ");

    }//GEN-LAST:event_insertButtonActionPerformed

    private void qImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qImageButtonActionPerformed
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);

        //This ensures only images are selected..        
        fc.addChoosableFileFilter(new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes()));

        //In response to a button click:
        int returnVal = fc.showDialog(MainScreen.mainFrame, "Choose Image");
        //Handle open button action.        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            questionImage = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + questionImage.getName() + ".");
        } else {
            System.out.println("Open command cancelled by user.");
            return;
        }

        qImageCheckBox.setText("Question Image: " + questionImage.getName());
    }//GEN-LAST:event_qImageButtonActionPerformed

    private void qImageCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qImageCheckBoxActionPerformed
        if (qImageCheckBox.isSelected()) {
            qImageButton.setEnabled(true);
        } else {
            qImageButton.setEnabled(false);
        }
    }//GEN-LAST:event_qImageCheckBoxActionPerformed

    private void answerCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_answerCheckBoxActionPerformed
        if (answerCheckBox.isSelected()) {
            answerTextArea.setEnabled(true);
            aImageCheckBox.setEnabled(true);
            if (aImageCheckBox.isSelected()) {
                aImageButton.setEnabled(true);
            }
        } else {
            answerTextArea.setEnabled(false);
            aImageCheckBox.setEnabled(false);
            aImageButton.setEnabled(false);
        }

    }//GEN-LAST:event_answerCheckBoxActionPerformed

    private void aImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aImageButtonActionPerformed
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);

        //This ensures only images are selected..        
        fc.addChoosableFileFilter(new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes()));

        //In response to a button click:
        int returnVal = fc.showDialog(MainScreen.mainFrame, "Choose Image");
        //Handle open button action.        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            answerImage = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + answerImage.getName() + ".");
        } else {
            System.out.println("Open command cancelled by user.");
            return;
        }

        aImageCheckBox.setText("Answer Image: " + answerImage.getName());
    }//GEN-LAST:event_aImageButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aImageButton;
    private javax.swing.JCheckBox aImageCheckBox;
    private javax.swing.JCheckBox answerCheckBox;
    private javax.swing.JTextArea answerTextArea;
    private javax.swing.JComboBox chapterComboBox;
    private javax.swing.JComboBox classComboBox;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel marksLabel;
    private javax.swing.JButton qImageButton;
    private javax.swing.JCheckBox qImageCheckBox;
    private javax.swing.JComboBox qTypeComboBox;
    private javax.swing.JTextArea questionTextArea;
    private javax.swing.JComboBox subjectComboBox;
    // End of variables declaration//GEN-END:variables

    private void updateClassComboBox() {
        classComboBox.removeAllItems();
        for (String s : myDBManager.getClasses(null)) {
            classComboBox.addItem(s);
        }
    }

    private void updateSubjectComboBox() {
        subjectComboBox.removeAllItems();
        String classx = classComboBox.getSelectedItem().toString();
        for (String s : myDBManager.getSubjects(null, classx)) {
            subjectComboBox.addItem(s);
        }
        updateChapterComboBox();
    }
    private void updateMarks() {
        DBManager db = DBManager.getInstance();
                int qTypeID = db.findQuestionTypeID(qTypeComboBox.getSelectedItem().toString().trim(),
                        db.findSubjectID(subjectComboBox.getSelectedItem().toString().trim(),
                                classComboBox.getSelectedItem().toString().trim(), null));
                ArrayList<String> idList = new ArrayList<>();
                idList.add(qTypeID+"");
                String marks = db.getQuestionTypeMarksByIDs(idList).get(0) + " Marks";
                marksLabel.setText(marks);
    }

    private void updateChapterComboBox() {
        chapterComboBox.removeAllItems();
        for (String s : myDBManager.getChapters(null,
                classComboBox.getSelectedItem().toString(),
                subjectComboBox.getSelectedItem().toString())) {
            chapterComboBox.addItem(s);
        }
    }

    private void updateQTypeComboBox() {
        qTypeComboBox.removeAllItems();
        ArrayList<String> ids = myDBManager.getQuestionTypeIDsForSubject(
                myDBManager.findSubjectID(subjectComboBox.getSelectedItem().toString(),
                        classComboBox.getSelectedItem().toString(), "default"));
        ArrayList<String> types = myDBManager.getQuestionTypeNamesByIDs(ids);
        for (String s : types) {
            qTypeComboBox.addItem(s);
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
}
