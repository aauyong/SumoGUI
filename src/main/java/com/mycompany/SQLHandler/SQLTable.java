/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.SQLHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author blarg
 */
public class SQLTable {

    private Map<String, Integer> headers;
    private List<List<String>> data;

    public SQLTable() {
    }

    public SQLTable(List<String> headers_) {
        this(headers_, Collections.<List<String>>emptyList()  );
    }

    public SQLTable(List<String> headers_, List<List<String>> data_) {
        this.headers = createHeaders(headers_);
        this.data = new ArrayList<>();
        for (var l : data_) {
            this.data.add(new ArrayList<>(l));
        }
    }

    public boolean insertRow(List<String> dataPt) {
        if (dataPt.size() != headers.size()) {
            return false;
        }
        return data.add(new ArrayList<>(dataPt){});
    }
    
    /**
     * Insert a set of data points, {@code dataPts}, into the data table. Every element
     * is checked to be within the size of the headers. If any data point has too many 
     * values, then the method returns false and nothing is added. 
     * @param dataPts
     * @return 
     */
    public boolean insertRows(List<List<String>> dataPts) {
        for (var l : dataPts) {
            if (l.size() != headers.size())
                return false; 
        }
        return data.addAll(new ArrayList<>(){{
            for (var elem : dataPts) {
                add(new ArrayList<>(elem));
            }
        }});
    }
    
    
    /**
     * Returns a Map of the headers to column indexs within the data
     * @param headers_
     * @return 
     */
    public final HashMap<String, Integer> createHeaders(List<String> headers_) {
        return new HashMap<>() {{
                for (int i = 0; i < headers_.size(); i++) {
                    put(headers_.get(i), i);
                }
            }};
    }
    
    /**
     * Get a value stored in the table at coordinates ({@code row}, {@code col}). Returns
     * null if either coord is out of range.
     * @param x Row coord to access
     * @param y Col coord to access
     * @return 
     */
    public String get(int x, int y) {
        if (x >= data.size() || y >= data.get(x).size()) {
            return null;
        }
        return data.get(x).get(y);
    }
    
    /**
     * Get the value stored in the table at {@code row} under column {@code col}.
     * Returns null {@code row} is out of range or {@code col} doesn't exist in the headers
     * @param row
     * @param col
     * @return 
     */
    public String get(int row, String col) {
        if (!headers.containsKey(col))
            return null;
        int loc = headers.get(col);
        return get(row,loc);
    }
    
    /**
     * Get the values stored in row, {@code row}. Returns null if {@code row} is out of
     * range.
     * @param row
     * @return 
     */
    public List<String> getRow(int row) {
        if (row >= data.size()) {
            return null;
        }
        return data.get(row);
    }
    
    public List<String> getCol(String col) {
        int colIdx = this.headers.get(col);
        ArrayList<String> colData = new ArrayList<>() {
            {
                for (var row : data) {
                    add(row.get(colIdx));
                }
            }
        };
        return colData;
    }
    
    /**
     * Getter for data points
     * @return 
     */
    public List<List<String>> getData() {
        return data;
    }
    
    public List<String> getHeaders() {
        return new ArrayList<>(this.headers.keySet());
    }
    
    public HashMap<String, Integer> getHeaderMap() {
        return new HashMap<>(this.headers);
    }
    
    public final int size() {
        return this.data.size();
    }
}
