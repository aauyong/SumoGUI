/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Tables;

import java.util.*;

/**
 * Banzuke table version of the CustomTable. Implements a method to insert a banzuke from
 * a list of key,value results
 * 
 * @author blarg
 */
public class BanzTbl extends CustomTable {

    public BanzTbl() {
        super( BANZUKE_HEADERS, BANZUKE_WIDTHS );
        this.fontFormatting("center");
    }
    
    /**
     * Inserts a banzuke into the table model. Parses {@code results} to discern which
     * side the wrestler should be placed on and if a new row should be created or not.
     * 
     * @param results 
     */
    public void insertBanzuke(List< Map<String, String> > results) {
        this.shrinkTable(0);
        String last_rank = "";
        String last_pos = "";
        Map<String, String> curr_line = new LinkedHashMap<>();
        for (var line : results) {
            String rank = line.get("rank_name");
            String pos = line.get("pos");
            String side = line.get("side");

            if (side.equals("") 
                    || !(rank.equals(last_rank) && pos.equals(last_pos)) ) {
                if (!curr_line.isEmpty()) {
                    this.insertRow(curr_line.values().toArray(String[]::new));
                }

                curr_line.put("e_record", "");
                curr_line.put("e_awd", "");
                curr_line.put("e_name", "");
                curr_line.put("rank", rank + pos);
                curr_line.put("w_name", "");
                curr_line.put("w_record", "");
                curr_line.put("w_awd", "");
            }

            last_rank = rank;
            last_pos = pos;

            String name = line.get("shikona");
            
            int abse = Integer.valueOf(line.get("absences"));
            String record = "%s - %s%s".formatted(
                    line.get("wins"),
                    line.get("losses"),
                    abse != 0 ? " - %d".formatted(abse) : "");
            
            String awrd = line.get("short_award");
            
            if (side.equals("")) {
                curr_line.replace("e_name", name);
                curr_line.replace("e_record", record);
                curr_line.replace("e_awd", awrd);
            } else {
                curr_line.replace("%s_name".formatted(side), name);
                curr_line.replace("%s_record".formatted(side), record);
                curr_line.replace("%s_awd".formatted(side), awrd);
            }
        }
        this.insertRow(curr_line.values().toArray(String[]::new));
    }
    
    public boolean insertBanzuke2(List<List<String>> data) {
        for( var d : data ) {
            if (d.size() < BANZUKE_HEADERS.size()) return false;
        }
            
        for ( List<String> d : data ) {
            if ( !insertRow( d.toArray(String[]::new) ) )
                return false;
        }
        
        return true;
    }
    
    
    public final static List<String> BANZUKE_HEADERS = List.of( "Record", "Awds", "East", 
            "Rank", "West", "Record", "Awds");
    public final static Map<String, Double> BANZUKE_WIDTHS = new HashMap<>() {{
        put("Record", 0.18);
        put("Awds", 0.1);
        put("East", 0.25);
        put("West", 0.25);
        put("Rank", 0.125);
    }};
}
