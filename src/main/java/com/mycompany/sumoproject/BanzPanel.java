/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sumoproject;

import javax.swing.JPanel;

/**
 *
 * @author blarg
 */
public class BanzPanel extends JPanel {
    /**
     * Constructor
     */
    public BanzPanel() {
        initComponents();
        initLayout();
    }

    /**
     * Get list of bashos from a given calendar year. Bashos numbers are left zero filled,
     * such that 1,3,5, and 9 are represented as 01,03, etc.
     *
     * @param year
     * @return List of two character strings representing the basho
     */
    protected void setBashos(String... yearOpt) {
        String year = yearOpt.length > 0 ? yearOpt[0] : banzYear.getSelectedItem().toString();
        String bashoSql = """
                          SELECT
                            DISTINCT( basho_num) AS basho
                          FROM dbo.fullResults
                          WHERE year = '%s'
                          ORDER BY basho_num;
                          """.formatted(year);
        banzBasho.removeAllItems();
        var qryTbl = SumoProject.sqlHandler.executeQuery(bashoSql);
        for (var b : qryTbl.getCol("basho")) {
            String bashoStr = b;
            if (b.length() < 2) {
                bashoStr = '0' + b;
            }
            banzBasho.addItem(bashoStr);
        }
    }

    protected void setYears() {
        String yearSql = """
                         SELECT DISTINCT
                            year
                         FROM simpleResults
                         ORDER BY year DESC;
                         """;
        var qryTbl = SumoProject.sqlHandler.executeQuery(yearSql);
        for (var y : qryTbl.getCol("year")) {
            banzYear.addItem(y);
        }
    }

    /**
     * Alloc. memory for components and initialize them
     */
    private void initComponents() {
        banzYear = new javax.swing.JComboBox<>();
        banzYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               banzYearActionPerformed(evt);
            }
        });

        banzBasho = new javax.swing.JComboBox<>();
        banzBasho.setModel(new javax.swing.DefaultComboBoxModel<>());

        banzButton = new javax.swing.JButton();
        banzButton.setText("Go");
        banzButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               banzButtonActionPerformed(evt);
            }
        });

        banzMCbox = new javax.swing.JCheckBox();
        banzMCbox.setText("Makuuchi");

        banzJCbox = new javax.swing.JCheckBox();
        banzJCbox.setText("Juryo");

        banzMsCbox = new javax.swing.JCheckBox();
        banzMsCbox.setText("Makushita");

        banzSdCbox = new javax.swing.JCheckBox();
        banzSdCbox.setText("Sandanme");

        banzJdCbox = new javax.swing.JCheckBox();
        banzJdCbox.setText("Jonidan");

        banzJkCbox = new javax.swing.JCheckBox();
        banzJkCbox.setText("Jonikuchi");

        banzMzCbox = new javax.swing.JCheckBox();
        banzMzCbox.setText("Mz/Bg");

        banzTbl = new com.mycompany.Tables.BanzTbl();
        banzTbl.setEnabled(false);
        banzTbl.setPreferredScrollableViewportSize(SizeConstants.TABLE);

        banzScroll = new javax.swing.JScrollPane();
        banzScroll.setViewportView(banzTbl);
    }


    /**
     * Initialize and organize the layout of the panel
     */
    private void initLayout() {
        setLayout(new java.awt.GridBagLayout());

        add(banzYear, new java.awt.GridBagConstraints());
        add(banzBasho, new java.awt.GridBagConstraints());
        add(banzMCbox, new java.awt.GridBagConstraints());
        add(banzJCbox, new java.awt.GridBagConstraints());
        add(banzMsCbox, new java.awt.GridBagConstraints());
        add(banzSdCbox, new java.awt.GridBagConstraints());

        add(banzJdCbox, new java.awt.GridBagConstraints() {
            {
                gridx = 2;
                gridy = 1;
            }
        });

        add(banzJkCbox, new java.awt.GridBagConstraints() {
            {
                gridx = 3;
                gridy = 1;
            }
        });

        add(banzMzCbox, new java.awt.GridBagConstraints() {
            {
                gridx = 4;
                gridy = 1;
            }
        });

        add(banzButton, new java.awt.GridBagConstraints() {
            {
                gridx = 5;
                gridy = 1;
            }
        });
        add(banzScroll, new java.awt.GridBagConstraints(){
            {
                gridx = 0;
                gridy = 2;
                gridwidth = 6;
            }
        });
    }


    /**
     * When the Banzuke Lookup Year is changed, update {@code banzBasho} to
     * contain only the available bashos
     * @param evt
     */
    private void banzYearActionPerformed(java.awt.event.ActionEvent evt) {
        String year = banzYear.getSelectedItem().toString();
        setBashos(year);
    }

    /**
     * Using the values from {@code banzYear} and {@code banzBasho}, query
     * the banzuke for that basho and store it into the table.
     * @param evt
     */
    private void banzButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String basho_string = banzYear.getSelectedItem().toString()
            + "."
            + banzBasho.getSelectedItem().toString();

        var results = Helpers.getBanzuke(basho_string, getBanzDivisionSelection());
        if (results == null) {
            return;
        }
        banzTbl.insertRows2(results);
        banzTbl.shrinkTable(results.size());
    }

    /**
     * Creates a T-SQL regex string to filter out or include specific divisions. If no
     * buttons are selected, all choices are included
     *
     * @return Regex string for each digit
     */
    private String getBanzDivisionSelection() {
        String s = "";

        s += banzMCbox.getSelectedObjects() != null ? '1' : "";
        s += banzJCbox.getSelectedObjects() != null ? '2' : "";
        s += banzMsCbox.getSelectedObjects() != null ? '3' : "";
        s += banzSdCbox.getSelectedObjects() != null ? '4' : "";
        s += banzJdCbox.getSelectedObjects() != null ? '5' : "";
        s += banzJkCbox.getSelectedObjects() != null ? '6' : "";
        s += banzMzCbox.getSelectedObjects() != null ? '7' : "";

        if (s.length() == 0) {
            return ALL_DIVISIONS;
        } else {
            return String.format("[%s]", s);
        }
    }


    private javax.swing.JComboBox<String> banzYear, banzBasho;
    private javax.swing.JButton banzButton;
    private javax.swing.JCheckBox banzMCbox, banzJCbox, banzMsCbox, banzSdCbox
                                ,banzJdCbox, banzJkCbox, banzMzCbox;
    private javax.swing.JScrollPane banzScroll;
    private com.mycompany.Tables.BanzTbl banzTbl;

    private final String ALL_DIVISIONS = "[0-9]";
}
