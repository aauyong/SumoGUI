/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.Tables;

import java.util.List;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author blarg
 */
public class CustomTable extends JTable {
    protected List<String> tblHdrs;
    protected DefaultTableModel tblMdl;
    
    public CustomTable() {
        this(List.of(""), new HashMap<String, Double>(){{ put("", 1.0);}});
    }
    
    public CustomTable(List<String> hdrs, Map<String, Double> colWidths, 
            DefaultTableModel... tm_arr) {
        tblHdrs = hdrs;
        
        DefaultTableModel tm;
        if (tm_arr.length > 0) {
            tm = tm_arr[0];
        }
        else {
            tm = new DefaultTableModel( tblHdrs.toArray(), 0);
        }
        tblMdl = tm;
        this.setModel(tblMdl);
        if (this.tblHdrs.size() > 1) {
            setWidths( colWidths, this.getPreferredScrollableViewportSize().width );
        }
        
        fontFormatting();
    }
    
    public DefaultTableModel getMdl() {
        return tblMdl;
    }
    
    public List<String> getHdrs() {
        return tblHdrs;
    }
    
        /**
     * Insert a row into the table at a provided index. If no index is provided, 
     * then {@code values} is inserted at the end of the table. If table is 
     * already of an appropriate size, the values are at {@code row} are 
     * replaced instead of a new row being inserted.
     * @param values List of values, inserted in order
     * @param row Optional location of row to insert
     * @return {@code true} if success
     */
    public final boolean insertRow(String[] values, int... row){
        if (values.length == 0)
            return false;

        int row_idx = row.length > 0 ? row[0] : tblMdl.getRowCount();
        if (tblMdl.getRowCount() <= row_idx) {
            tblMdl.insertRow(row_idx, values);
        }
        else {
            for (int i = 0; i < values.length; i++) {
                tblMdl.setValueAt(values[i], row_idx, i);
            }
        }
        return true;
    }
    
    /**
     *
     * @param lines
     * @param order
     * @param startRow
     * @return
     */
    public boolean insertRows(List< Map<String, String> > lines, List<String> order, 
            int... startRow) {
        int start = startRow.length > 0 ? startRow[0] : 0;
        String[] values = new String[order.size()];
        for (var line : lines) {
            for (int i = 0; i < order.size(); i++) {
                values[i] = line.get(order.get(i));
            }
            this.insertRow(values, start);
            start++;
        }
        return true;
    }
    
    public boolean insertRows2(List<List<String>> data, Map<String, Integer> order, 
            int... startRow) {
        int start = startRow.length > 0 ? startRow[0] : 0;
        String[] values = new String[order.size()];
        for (var row : data) {
            for (var key : order.keySet()) {
                values[order.get(key)] = row.get(order.get(key));
            }
            this.insertRow(values, start);
            start++;
        }
        return true;
    }
    
        public boolean insertRows2(List<List<String>> data, int... startRow) {
        int start = startRow.length > 0 ? startRow[0] : 0;
        for (var row : data) {
            this.insertRow(row.toArray(String[]::new), start);
            start++;
        }
        return true;
    }
    
    /**
     * Shrinks table to a desired size by removing rows
     * @param desiredSize 
     */
    public final void shrinkTable(int desiredSize ) {
        Integer currSize = tblMdl.getRowCount();
        while (currSize > desiredSize) {
            tblMdl.removeRow( currSize - 1);
            currSize--;
        }
    }
    
    // Private Methods
    /**
     * Formats font of the table to center it;
     * @param opts
     */
    protected final void fontFormatting(String... opts) {
        var colMdl = this.getColumnModel();
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0,
                    Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                Component tableCellRendererComponent = super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
                int align;
                if (opts.length > 0 && opts[0].toLowerCase() == "center'") 
                    align = DefaultTableCellRenderer.CENTER;
                else {
                    align = DefaultTableCellRenderer.LEFT;
                }
                ((DefaultTableCellRenderer) tableCellRendererComponent).setHorizontalAlignment(align);
                return tableCellRendererComponent;
            }
        };

        for (int i = 0; i < colMdl.getColumnCount(); i++) {
            colMdl.getColumn(i).setCellRenderer(renderer);
        }
}
     
    /**
     * Update table column widths to fill a given size
     * @param widths
     * @param totalSize 
     */
    protected final void setWidths(Map<String, Double> widths, int totalSize) {
        var colMdl = this.getColumnModel();
        for (int i = 0; i < this.tblHdrs.size(); i++) {           
            colMdl.getColumn(i).setPreferredWidth(
                    (int) (totalSize * widths.get(this.tblHdrs.get(i))) 
            );
        }
    }
}
