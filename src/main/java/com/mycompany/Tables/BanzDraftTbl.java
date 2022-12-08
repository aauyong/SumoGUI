package com.mycompany.Tables;

import javax.swing.DefaultListSelectionModel;
import com.mycompany.Tables.BanzTbl.*;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// TODO set up doing some formatting for selected wrestlers

/**
 * Banzuke table for drafting. Includes methods for selection validation and
 * getting
 *
 * @author blarg
 */
public class BanzDraftTbl extends BanzTbl {

    public BanzDraftTbl() {
        this(null, null);
    }
    
    public BanzDraftTbl(String basho, Integer division) {
        this.basho = basho;
        this.division = division;
                
        setupSelectionModel(BANZUKE_HEADERS.indexOf("East"),
            BANZUKE_HEADERS.indexOf("West"));

        this.setColumnSelectionAllowed(true);

        this.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        this.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                clearSelection();
            }
        });
    }

    /**
     * Get the value of the currently selected cell as a String.
     *
     * If the indices of the row or column -1 for any reason, value is null
     * @return
     */
    public String getSelectedValue() {
        int row = this.getSelectedRow();
        if (row < 0) return null;

        int col = this.getSelectedColumn();
        if (col < 0) return null;

        return (String) getValueAt(this.getSelectedRow(), this.getSelectedColumn());
    }

    public String getSelectedCol(int col) {
        return (String) getValueAt(this.getSelectedRow(), col);
    }

    public String getSelectedCol(String colName) {
        int idx = BANZUKE_HEADERS.indexOf(colName);
        return (String) getValueAt(this.getSelectedRow(), idx);
    }

    /**
     * Getter for the table's Basho. Will return null if Basho isn't set
     * @return String or {@code Null}
     */
    public String getBasho() {
        return basho;
    }
    
    /**
     * Getter for table's division. Will return null if division isn't set
     * @return Integer or {@code Null}
     */
    public Integer getDivision() {
        return division;
    }

    /**
     * Method to set up column selection model to only allow a single cell selection and
     * only allow selection of specific
     *
     * @param col1
     * @param col2
     */
    private void setupSelectionModel(int col1, int col2) {
        this.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.getColumnModel().setSelectionModel(new DefaultListSelectionModel() {

            private boolean isSelectable(int index0, int index1) {
                return (index0 == index1) && (index0 == col1 || index0 == col2);
            }

            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (isSelectable(index0, index1)) {
                    super.setSelectionInterval(index0, index1);
                }
            }
        });
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Division that the table represents
     */
    private final Integer division;

    /**
     * Basho that the table represents
     */
    private final String basho;
}
