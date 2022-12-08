/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sumoproject;

import com.mycompany.SQLHandler.SQLTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author blarg
 */
public class Helpers {
    /**
     * Check whether the input is a wrestler id, shikona, or full name, and
     * returns a map containing the three pieces of data
     *
     * @param input string  to search with
     * @return A String hash map containing the id, name, and shikona
     */
    protected static Map<String, String> validateNameIdInput(String input) {
        String sql = "SELECT id,full_shikona,shikona FROM idRikishiMap WHERE %s = %s";
        try {
            Integer check = Integer.valueOf(input);
            sql = sql.formatted("id", check);
        } catch (NumberFormatException e) {
            if (input.contains(" ")) {
                sql = sql.formatted("full_shikona", "\'" + input + "\'");
            } else {
                sql = sql.formatted("shikona", "\'" + input + "\'");
            }
        }
        
        SQLTable queryTbl = SumoProject.sqlHandler.executeQuery(sql);

        Map<String, String> results = new HashMap<>(){
            {
                queryTbl.getHeaders().forEach(
                        key -> { put( key, queryTbl.get(0,key) ); }
                );
            }
        };
        return results;
    }

    /**
     * Generate a Matchup record string between two wrestler ids, {@code east} and 
     * {@code west}. Returns the string in the form 
     * W [Absent W] (Playoff W) - L [Absent L] (Playoff L)
     * 
     * @param eastId east/left Id number
     * @param westId west/right Id number
     * @return 
     */
    protected static String generateMURecordString(Integer eastId, Integer westId) {
        String muRecord = "";
        String muResults;
        muResults = "SELECT "
                + "COUNT(CASE WHEN day <= 15 AND result = 'O' THEN 1 ELSE NULL END) AS W, "
                + "COUNT(CASE WHEN day <= 15 AND result = 'X' THEN 1 ELSE NULL END) AS L, "
                + "COUNT(CASE WHEN result = 'Z' THEN 1 ELSE NULL END) AS AW, "
                + "COUNT(CASE WHEN result = 'A' THEN 1 ELSE NULL END) AS AL, "
                + "COUNT(CASE WHEN day > 15 AND result = 'O' THEN 1 ELSE NULL END) as PW, "
                + "COUNT(CASE WHEN day > 15 AND result = 'X' THEN 1 ELSE NULL END) as PL "
                + "FROM simpleMatchups "
                + "WHERE east_id = %d AND west_id = %d".formatted(eastId, westId);
        var qryTbl = SumoProject.sqlHandler.executeQuery(muResults);
        Integer absentW = Integer.valueOf(qryTbl.get(0, "AW"));
        String absentWStr = absentW > 0 ? "[+%d]".formatted(absentW) : "";

        Integer absentL = Integer.valueOf(qryTbl.get(0, "AL"));
        String absentLStr = absentL > 0 ? "[+%d]".formatted(absentL) : "";

        Integer playoffW = Integer.valueOf(qryTbl.get(0, "PW"));
        String playoffWStr = playoffW > 0 ? "(+%d)".formatted(playoffW) : "";

        Integer playoffL = Integer.valueOf(qryTbl.get(0, "PL"));
        String playoffLStr = playoffL > 0 ? "(+%d)".formatted(playoffL) : "";

        muRecord =  "%s%s%s - %s%s%s".formatted(
                qryTbl.get(0, "W"),
                absentWStr,
                playoffWStr,
                qryTbl.get(0,"L"),
                absentLStr,
                playoffLStr
         );
        return muRecord;
    }

    /**
     * get Banzuke for a given {@code basho} and division filtering as designated by
     * {@code division_filter}
     * 
     * @param basho Basho string in the format yyyy.bb, where bb is the zero-padded month
     * @param division_filter Sql pattern string indicating which divisions to include
     * @return 
     */
    protected static List< List<String> > getBanzuke(String basho,
             String... division_filter) {
        String division = division_filter.length > 0 ? division_filter[0] : "[0-9]";
        
        String lookupSql = """
                    EXECUTE dbo.upc_getBanzuke '%s', '%s'
                """.formatted(basho, division);
        
        var qryTbl = SumoProject.sqlHandler.executeQuery(lookupSql);
        return qryTbl.getData();
    }
    
    protected static String getMostRecentBasho() {
        String qry;
        qry = """
              SELECT DISTINCT TOP(1)
              	basho
              FROM fullResults
              ORDER BY basho DESC
              """;
        var qryTbl = SumoProject.sqlHandler.executeQuery(qry);
        return qryTbl.get(0,0);
    }
}
