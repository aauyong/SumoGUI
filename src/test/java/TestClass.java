/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.mycompany.SQLHandler.SQLHandler;
import java.sql.SQLException;

/**
 *
 * @author blarg
 */
public class TestClass {
    public static void main(String[] args) {
        String server = "NOTTAMACK\\SQLEXPRESS";
        String databaseName = "TutorialDB";
        SQLHandler handler = null;
        try{
            handler = new SQLHandler(server, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String query = """
                       SELECT * FROM fullResults WHERE id = 0
                       """;
        var map = handler.executeQuery(query);
        System.out.println("");
    }
}
