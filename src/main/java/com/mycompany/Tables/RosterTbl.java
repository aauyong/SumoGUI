/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Tables;

import java.util.List;

/**
 *
 * @author blarg
 */
public class RosterTbl extends CustomTable {
    public RosterTbl() {
    }
    
    public static List<String> ROSTER_HEADERS = List.of( "Wrestler", "Rank", "Wins", 
            "Losses", "Points" );
}
