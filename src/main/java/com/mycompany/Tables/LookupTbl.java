/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author blarg
 */
public class LookupTbl extends CustomTable {

    private int currRikishiId = -1;
    
    public LookupTbl() {
        super(LOOKUP_HEADERS, LOOKUP_WIDTHS);
        setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }

    public final boolean isSameRikishi(Integer i) {
        boolean res = currRikishiId == i;
        currRikishiId = i;
        return res;
    }
    
    public final int getCurrRikishiId() {
        return currRikishiId;
    }
    
    public final String getSelectedBasho() {
        int row = this.getSelectedRow();
        try {
            return (String) this.getValueAt(row, 1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    public final static List<String> LOOKUP_HEADERS = List.of(
            "Shikona","Basho","Rank","Record","W-L-A","Award");
    
    public final static Map<String, Double> LOOKUP_WIDTHS = new HashMap<>() {{
        put("Shikona", 0.18);
        put("Basho", 0.12);
        put("Rank", 0.125);
        put("Record", 0.33);
        put("W-L-A", 0.12);
        put("Award", 0.09);
    }};
    
}
