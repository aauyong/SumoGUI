/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sumoproject;

import com.mycompany.DraftHandler.DraftHandler;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author blarg
 */
public class DraftPanel extends JPanel{

    public DraftPanel() {
        initMembers();
        initLayout();
    }

    /**
     * Initialize members as new class objects
     */
    private void initMembers() {
        numOfDrftrsSpnnr = new javax.swing.JSpinner();
        numOfDrftrsSpnnr.setModel(
            new javax.swing.SpinnerNumberModel(
                3, 1, 10, 1)
        );
        numOfDrftrsSpnnr.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numOfDraftersStateChanged(evt);
            }
        });

        numDrftrSpnnrLbl = new javax.swing.JLabel();
        numDrftrSpnnrLbl.setLabelFor(numOfDrftrsSpnnr);
        numDrftrSpnnrLbl.setText("Number of Drafters");

        setupBut = new javax.swing.JButton();
        setupBut.setText("Set Up Draft");
        setupBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                draftSetupButActionPerformed(evt);
            }
        });

        makNumSpnnr = new javax.swing.JSpinner();
        makNumSpnnr.setModel(
            new javax.swing.SpinnerNumberModel(
                5, 1, 21, 1)
        );

        makNumSpnnrLbl = new javax.swing.JLabel();
        makNumSpnnrLbl.setLabelFor(makNumSpnnr);
        makNumSpnnrLbl.setText("Number of Makuuchi Wrestlers");

        juryoNumSpnnr = new javax.swing.JSpinner();
        juryoNumSpnnr.setModel(
            new javax.swing.SpinnerNumberModel(
                0, 0, 21, 1)
        );

        juryoNumSpnnrLbl = new javax.swing.JLabel();
        juryoNumSpnnrLbl.setLabelFor(juryoNumSpnnr);
        juryoNumSpnnrLbl.setText("Number of Juryo Wrestlers");

        isRandCBox = new javax.swing.JCheckBox();

        isRandCBoxLbl = new javax.swing.JLabel();
        isRandCBoxLbl.setLabelFor(isRandCBox);
        isRandCBoxLbl.setText("Randomized Draft");

        draftType = new javax.swing.JComboBox<>();
        draftType.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "Linear", "Snake" }
        ));

        draftTypeLbl = new javax.swing.JLabel();
        draftTypeLbl.setText("Draft Type");
        draftTypeLbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        playersTblScrll = new javax.swing.JScrollPane();
        playersTbl = new javax.swing.JTable();

        playersTblScrll.setPreferredSize(new java.awt.Dimension(100, 275));
        playersTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Drafter 1"},
                {"Drafter 2"},
                {"Drafter 3"}
            },
            new String [] {
                "Players"
            }
        ) {
            // Class[] types = new Class [] {
            //     java.lang.String.class
            // };

            // public Class getColumnClass(int columnIndex) {
            //     return types [columnIndex];
            // }
        });
        playersTbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        playersTbl.getColumnModel().getColumn(0).setResizable(false);

        playersTblScrll.setViewportView(playersTbl);
    }

    /**
     * Initialize and set-up the layout
     * 
     *    0 1 2 3 4 5 6 7 8
     * 0: A |   |   |   |
        * 1:---|---|---|---|--
        * 2: a |   |   | B | C
        * 3:---|---|---|---|--
        * 4: a |   |   | D | E
        * 5:---|---|---|---|--
        * 6: a |   |   | F | G
        * 7:---|---|---|---|--
        * 8: a |   |   | H | I
        * 9:---|---|---|---|--
        *10: a |   |   | J | K
        *11:---|---|---|---|--
        *12: a |   |   | L | l
        *13:---|---|---|---|--
     */
    private void initLayout() {
        setLayout( new java.awt.GridBagLayout(){
            {
                columnWidths = new int[] {
                    0, 5, 0, 5, 0, 5, 0, 5, 0
                };
                rowHeights = new int[] {
                    0, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5
                };
            }
        });

        // A
        add(playersTblScrll, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 0;
                gridheight = 13;
            }
        });

        // B
        add(numOfDrftrsSpnnr, new java.awt.GridBagConstraints() {
            {
                gridx = 6;
                gridy = 2;
                anchor = java.awt.GridBagConstraints.LINE_END;
            }
        });

        // C
        add(numDrftrSpnnrLbl, new java.awt.GridBagConstraints() {
            {
                gridx = 8;
                gridy = 2;
                anchor = java.awt.GridBagConstraints.LINE_START;
            }
        });

        // D
        add(makNumSpnnr, new java.awt.GridBagConstraints() {
            {
                gridx = 6;
                gridy = 4;
                anchor = java.awt.GridBagConstraints.LINE_END;
            }
        });

        // E
        add(makNumSpnnrLbl, new java.awt.GridBagConstraints() {
            {
                gridx = 8;
                gridy = 4;
                anchor = java.awt.GridBagConstraints.LINE_START;
            }
        });

        // F
        add(juryoNumSpnnr, new java.awt.GridBagConstraints() {
            {
                gridx = 6;
                gridy = 6;
                anchor = java.awt.GridBagConstraints.LINE_END;
            }
        });

        // G
        add(juryoNumSpnnrLbl, new java.awt.GridBagConstraints() {
            {
                gridx = 8;
                gridy = 6;
                anchor = java.awt.GridBagConstraints.LINE_START;
            }
        });

        // H
        add(isRandCBox, new java.awt.GridBagConstraints() {
            {
                gridx = 6;
                gridy = 8;
                anchor = java.awt.GridBagConstraints.LINE_END;
            }
        });

        // I
        add(isRandCBoxLbl,  new java.awt.GridBagConstraints() {
            {
                gridx = 8;
                gridy = 8;
                anchor = java.awt.GridBagConstraints.LINE_START;
            }
        });

        // J
        add(draftType, new java.awt.GridBagConstraints() {
            {
                gridx = 6;
                gridy = 10;
                anchor = java.awt.GridBagConstraints.LINE_END;
            }
        });

        // K
        add(draftTypeLbl,  new java.awt.GridBagConstraints() {
            {
                gridx = 8;
                gridy = 10;
                anchor = java.awt.GridBagConstraints.LINE_START;
            }
        });

        // L
        add(setupBut, new java.awt.GridBagConstraints() {
            {
                gridx = 6;
                gridy = 12;
                gridwidth = 3;
            }
        });
    }

    /**
     * Adjusts the table size according to the value of {@code numOfDrafters}. Default
     * values are set to the Drafter + row number.
     * @param evt
     */
    private void numOfDraftersStateChanged(javax.swing.event.ChangeEvent evt) {
        int players = (int)numOfDrftrsSpnnr.getValue();

        DefaultTableModel tm = (DefaultTableModel)playersTbl.getModel();
        int rowCnt = playersTbl.getRowCount();

        if (players > rowCnt) {
            tm.insertRow(rowCnt++, new String[]{"Drafter %d".formatted(players)});
        }
        else if (players < rowCnt) {
            tm.removeRow(rowCnt - 1);
            rowCnt--;
        }
    }

    private void draftSetupButActionPerformed(java.awt.event.ActionEvent evt) {
        int numDrafters = (int)numOfDrftrsSpnnr.getValue();
        int mNum = (int)makNumSpnnr.getValue();

        if ( ( mNum * numDrafters ) > 42 ) {
            JOptionPane.showMessageDialog(
                this,
                "Too many Makuuchi Wrestlers given number of players"
            );
            return;
        }

        int jNum = (int)juryoNumSpnnr.getValue();
        if ( (jNum * numDrafters ) > 28 ) {
            JOptionPane.showMessageDialog(
                this,
                "Too many Juryo Wrestlers given number of players"
            );
            return;
        }
        
        List<Integer> divisionSizes = new ArrayList<>(){
            {
                if (mNum > 0) add(mNum);
                if (jNum > 0) add(jNum);
            }
        };

        List<String> users = new ArrayList<>() {{
            for (int i = 0; i < playersTbl.getRowCount(); i++) {
                add( (String) playersTbl.getValueAt(i, 0) );
            }
        }};
        draftDialog = new DraftDialog(
                        SwingUtilities.getWindowAncestor(this),
                        JDialog.ModalityType.APPLICATION_MODAL,
                        users,
                        divisionSizes.stream().mapToInt(Integer::intValue).toArray(),
                        isRandCBox.isSelected(),
                        (String) draftType.getSelectedItem()
                );
        draftDialog.pack();
        draftDialog.setVisible(true);
        
        draftDialog = null;
    }
   
    /**
     * Draft Handler for running and handling the Draft
     */
    protected DraftHandler draftHandler;

    /**
     * Draft Dialog window used for displaying a customized draft
     */
    private DraftDialog draftDialog;

    /**
     * Spinner for setting the number of drafters/players
     */
    private javax.swing.JSpinner numOfDrftrsSpnnr;

    /**
     * Label for {@code numOfDrftsSpnnr}
     */
    private javax.swing.JLabel numDrftrSpnnrLbl;

    /**
     * Spinner for setting the number of Makuuchi Wrestlers
     */
    private javax.swing.JSpinner makNumSpnnr;

    /**
     * Label for {@code makNumSpnnr}
     */
    private javax.swing.JLabel makNumSpnnrLbl;

    /**
     * Spinner for setting the number of Juryo Wrestlers
     */
    private javax.swing.JLabel juryoNumSpnnrLbl;

    /**
     * Label for {@code juryoNumSpnnr}
     */
    private javax.swing.JSpinner juryoNumSpnnr;

    /**
     * Scroll Pane for Players Table
     */
    private javax.swing.JScrollPane playersTblScrll;

    /**
     * Table for displaying and editing the list of players
     */
    private javax.swing.JTable playersTbl;

    /**
     * Type of draft, such as Linear or Snake
     */
    private javax.swing.JComboBox<String> draftType;

    /**
     * Label for {@code draftType}
     */
    private javax.swing.JLabel draftTypeLbl;

    /**
     * Whether Draft is randomized or follows the draft order
     */
    private javax.swing.JCheckBox isRandCBox;

    /**
     * Label for {@code isRandCBox}
     */
    private javax.swing.JLabel isRandCBoxLbl;

    /**
     * Button to confirm settings and set up Draft
     */
    private javax.swing.JButton setupBut;
    
}
