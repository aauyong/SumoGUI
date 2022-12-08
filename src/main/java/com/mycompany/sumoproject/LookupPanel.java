/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sumoproject;

import com.mycompany.sumoproject.Helpers;
import com.mycompany.sumoproject.SizeConstants;
import com.mycompany.sumoproject.SumoProject;
import javax.swing.JPanel;

import java.awt.GridBagConstraints;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author blarg
 */
public class LookupPanel extends JPanel {
    /**
     * Constructor
     */
    public LookupPanel() {
        initMembers();
        initLayout();
    }

    /**
     * Initialize members as new class objects
     */
    private void initMembers() {
        initLuRikishiName();
        initLuButton();
        initLuBashoResultsBut();
        initLuIdInput();
        luResultsTbl = new com.mycompany.Tables.LookupTbl();
        initLuScroll();
    }

    /**
     * Initialize the layout
     */
    private void initLayout() {
        setLayout(new java.awt.GridBagLayout());

        add(luRikishiName, new java.awt.GridBagConstraints(){
            {
                gridx = 0;
                gridy = 1;
                gridwidth = 5;
            }
        });
        
        add(luButton, new java.awt.GridBagConstraints() {
            {
                gridx = 2;
                gridy = 0;
                weightx = 0.1;
            }
        });

        add(luIdInput, new java.awt.GridBagConstraints() {
            {
                this.gridx = 0;
                this.gridy = 0;
                this.weightx = 0.1;
            }
        });

        add(luBashoResultsBut, new java.awt.GridBagConstraints(){
            {
                gridx = 1;
                gridy = 3;
            }
        });

        add(luScroll, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 2;
                gridwidth = 3;
                weightx = 1.0;
                weighty = 1.0;
            }
        });
    }

    /**
     * Initialize the textfield containing the name of the rikishi to lookup
     */
    private void initLuRikishiName() {
        luRikishiName = new javax.swing.JLabel();
        luRikishiName.setFont(new java.awt.Font("Segoe UI", 0, 24));
        luRikishiName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        luRikishiName.setText("Name");
        luRikishiName.setPreferredSize(SizeConstants.TITLE_TXT);
    }


    /**
     * Initialize the Lookup Button to search for results
     */
    private void initLuButton() {
        luButton = new javax.swing.JButton();
        luButton.setText("Lookup");
        luButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            luButtonActionPerformed(evt);
        });
    }

    /**
     * Lookup Tab button : Using the name or id within the text field,
     * {@code luIdInput}, lookup results of the wrestler throughout their career
     * and insert it into the {@code luResultsTbl}. Will not update table if
     * the same rikishi is queried.
     *
     * @param evt
     */
    private void luButtonActionPerformed(java.awt.event.ActionEvent evt) {
        var results = Helpers.validateNameIdInput(luIdInput.getText());

        if (results == null) {
            luRikishiName.setText("Does Not Exist");
            return;
        }
        else if (luResultsTbl.isSameRikishi( Integer.valueOf(results.get("id"))) ) {
            return;
        }
        Integer id = Integer.valueOf(results.get("id"));
        luRikishiName.setText(results.get("full_shikona"));

        // Display results
        String lookupSql;
        lookupSql = """
                    SELECT
                        shikona as Shikona
                        ,basho as Basho
                        ,rank as Rank
                        ,Record = day1 + day2 + day3 + day4
                            + day5 + day6 + day7 + day8
                            + day9 + day10 + day11 + day12
                            + day13 + day14 + day15
                        ,CAST(wins AS VARCHAR(2)) 
                                    + '-' + CAST(losses AS VARCHAR(2)) 
                                    + (CASE 
                                            WHEN absences > 0 THEN '-' + CAST(absences AS VARCHAR(2))
                                            ELSE ''
                                    END) AS [W-L-A]
                        ,awards as Awards
                    FROM simpleResults
                    WHERE id = %d
                    """.formatted(id);
        var queryTbl = SumoProject.sqlHandler.executeQuery(lookupSql);
        luResultsTbl.insertRows2(
                queryTbl.getData()
                ,queryTbl.getHeaderMap()
        );
    }

    
    /**
     * Initalize the Lookup Button to show Selected Basho Results
     */
    private void initLuBashoResultsBut() {
        luBashoResultsBut = new javax.swing.JButton();
        luBashoResultsBut.setText("Get Basho Results");
        luBashoResultsBut.addActionListener((java.awt.event.ActionEvent evt) -> {
            luBashoResultsButActionPerformed(evt);
        });
    }


    /**
     * Initalize the ID input textfield
     */
    private void initLuIdInput() {
        luIdInput = new javax.swing.JTextField();
        luIdInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        luIdInput.setPreferredSize(SizeConstants.TXT);
    }


    /**
     * Initialize the Lookup Scrollpane
     */
    private void initLuScroll() {
        luScroll = new javax.swing.JScrollPane();
        luScroll.setPreferredSize(SizeConstants.TABLE);
        luScroll.setViewportView(luResultsTbl);
    }


    /**
     * Creates a dialog box containing the results for the looked up wrestler and
     * the given basho.
     *
     *
     * @param evt
     */
    private void luBashoResultsButActionPerformed(java.awt.event.ActionEvent evt) {
        String basho = luResultsTbl.getSelectedBasho();
        if (basho == null)
            return;
        int id_ = luResultsTbl.getCurrRikishiId();
        String lookupSql;
        lookupSql = """
                    SELECT
                        basho AS Basho,
                        day AS Day,
                        east AS Shikona,
                        rank AS idRank,
                        result AS Result,
                        west AS Opponent,
                        oppRank,
                        kimarite AS Kimarite
                    FROM simpleMatchups
                    WHERE east_id = '%s' AND basho = '%s'
                    ORDER BY day
                    """.formatted(id_, basho);
        var queryTbl = SumoProject.sqlHandler.executeQuery(lookupSql);
        initLookupDialog(
                id_
                ,basho
                ,queryTbl.getData()
        );
    }

    
    /**
     * Initialize and display a dialog window that shows the results for a the selected
     * basho. Takes {@code id} and {@code basho} to display the title of the screen
     * @param id
     * @param basho
     * @param data 
     */
    private void initLookupDialog(Integer id, String basho, List<List<String>> data) {
        luDialog = new javax.swing.JDialog();
        luDialogScroll = new javax.swing.JScrollPane();
        luBashoResultsTbl = new com.mycompany.Tables.MatchupTbl();

        luBashoResultsTbl.insertRows2(data);

        luDialogScroll.setViewportView(luBashoResultsTbl);

        luDialog.setResizable(false);
        luDialog.getContentPane().add(luDialogScroll, java.awt.BorderLayout.CENTER);
        luDialog.setTitle("%s - %s".formatted(id, basho));
        luDialog.pack();
        luDialog.setVisible(true);
    }

    private javax.swing.JLabel luRikishiName;
    private javax.swing.JButton luButton, luBashoResultsBut;
    private javax.swing.JTextField luIdInput;
    private com.mycompany.Tables.LookupTbl luResultsTbl;
    private javax.swing.JScrollPane luScroll;

    private javax.swing.JDialog luDialog;
    private com.mycompany.Tables.MatchupTbl luBashoResultsTbl;
    private javax.swing.JScrollPane luDialogScroll;
}
