/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sumoproject;

import java.sql.SQLException;
import javax.swing.JPanel;

/**
 *
 * @author blarg
 */
public class MatchupPanel extends JPanel {
    /**
     * Constructor
     */
    public MatchupPanel() {
        initComponents();
        initLayout();
    }

    /**
     * Alloc. memory for components and initialize them
     */
    private void initComponents() {
        muRikishiA = new javax.swing.JTextField();
        muRikishiA.setPreferredSize(SizeConstants.TXT);
        muRikishiA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                muRikishiAKeyReleased(evt);
            }
        });

        muRikishiAName = new javax.swing.JLabel();
        muRikishiAName.setText("Wrestler");

        muRikishiB = new javax.swing.JComboBox<>();
        muRikishiB.setEditable(true);
        muRikishiB.setPreferredSize(new java.awt.Dimension(150,25));

        muRecord = new javax.swing.JLabel();
        muRecord.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        muRecord.setPreferredSize(SizeConstants.TITLE_TXT);
        muRecord.setHorizontalAlignment(0);
        muRecord.setText("Record");

        muLookupButton = new javax.swing.JButton();
        muLookupButton.setText("Lookup");
        muLookupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muLookupButtonActionPerformed(evt);
            }
        });

        muTextExplanation = new javax.swing.JLabel();
        muTextExplanation.setText("W [Absent W] (Playoff W) - L [Absent L] (Playoff L)");

        muTbl = new com.mycompany.Tables.MatchupTbl();
        muScroll = new javax.swing.JScrollPane();
        muScroll.setPreferredSize(SizeConstants.TABLE);
        muTbl.setPreferredScrollableViewportSize(SizeConstants.TABLE);
        muScroll.setViewportView(muTbl);
    }

    
    /**
     * Initialize and organize the layout of the panel
     */
    private void initLayout() {
        setLayout(new java.awt.GridBagLayout());

        add(muRikishiA, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 0;
                weightx = 0.1;
            }
        });

        add(muRikishiB, new java.awt.GridBagConstraints() {
            {
                gridx = 1;
                gridy = 0;
                weightx = 0.1;
            }
        });
        
        add(muRecord, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 2;
                gridwidth = 2;
            }
        });
        
        add(muLookupButton, new java.awt.GridBagConstraints() {
            {
                gridx = 1;
                gridy = 1;
            }
        });

        add(muTextExplanation, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 3;
                gridwidth = 2;
            }
        });

        add(muScroll, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 4;
                gridwidth = 2;
            }
        });

        add(muRikishiAName,new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 1;
            }
        });
    }

    
    /**
     * Fills in unique opponents for the rikishi in slot A. If rikishi does not
     * exist, then nothing is done.
     * @param evt
     */
    private void muRikishiAKeyReleased(java.awt.event.KeyEvent evt) {
        var results = Helpers.validateNameIdInput(muRikishiA.getText());
        if (results == null) {
            muRikishiAName.setText("Does Not Exist");
            return;
        }
        muRikishiAName.setText(results.get("shikona"));

        String opponentsSql;
        opponentsSql = """
                        WITH uniqueOpponents AS (
                            SELECT DISTINCT
                            west_id
                            FROM dbo.simpleMatchups where east_id = %s
                        )
                        SELECT
                        shikona = (CASE WHEN title > 1
                            THEN shikona + ' (' +  CONVERT(VARCHAR(4), hatsu) + ')'
                            ELSE shikona
                            END)
                        FROM uniqueOpponents INNER JOIN dbo.idRikishiMap
                        ON west_id = id
                        ORDER BY shikona
                        """.formatted( results.get("id") );
        var qryTbl = SumoProject.sqlHandler.executeQuery(opponentsSql);
        muRikishiB.removeAllItems();
        for (var data : qryTbl.getData()) {
            muRikishiB.addItem(data.get(0));
        }
    }


    /**
     * Lookup matchups between rikishi A and rikishi B and insert the resulting
     * matchups into the matchup table.
     * @param evt
     */
    private void muLookupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        var resultsLeft = Helpers.validateNameIdInput(muRikishiA.getText());
        var resultsOpp = Helpers.validateNameIdInput(muRikishiB.getSelectedItem().toString());
        if (resultsLeft == null || resultsOpp == null) {
            muRecord.setText("Error finding wrestlers");
            return;
        }
        muRecord.setText( Helpers.generateMURecordString(
            Integer.valueOf(resultsLeft.get("id"))
            ,Integer.valueOf(resultsOpp.get("id"))
        ));

        String matchHistorySql;
        matchHistorySql = """
            SELECT basho,day,east,rank,result,west,oppRank,kimarite
            FROM simpleMatchups
            WHERE east_id = %s AND west_id = %s
            """.formatted( resultsLeft.get("id"), resultsOpp.get("id") );
       
        var qryTbl = SumoProject.sqlHandler.executeQuery(matchHistorySql);
        muTbl.insertRows2(qryTbl.getData(), qryTbl.getHeaderMap());
        muTbl.shrinkTable(qryTbl.size());
    }

    private javax.swing.JTextField muRikishiA;
    private javax.swing.JComboBox<String> muRikishiB;
    private javax.swing.JLabel muRecord, muTextExplanation, muRikishiAName, muTest;
    private javax.swing.JButton muLookupButton;
    private com.mycompany.Tables.MatchupTbl muTbl;
    private javax.swing.JScrollPane muScroll;
}
