/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Tables;

import java.awt.Component;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author blarg
 */
public class MatchupTbl extends CustomTable {

    public MatchupTbl() {
        super(MATCHUP_HEADERS, MATCHUP_WIDTHS );
    }
    
    public final static List<String> MATCHUP_HEADERS = List.of(
            "Basho", "Day", "Shikona", "Rank", "Result",
            "Opponent", "Rank", "Kimarite"
    );
    
    public final static Map<String, Double> MATCHUP_WIDTHS = new HashMap<>() {{
        put("Basho", 0.18);
        put("Day", 0.07);
        put("Shikona", 0.18);
        put("Rank", 0.125);
        put("Opponent", 0.18);
        put("Kimarite", 0.18);
        put("Result", 0.07);
    }};
    
}
