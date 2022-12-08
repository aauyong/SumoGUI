/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.SQLHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Handler for interacting with the SQL database
 *
 * @author blarg
 */
public class SQLHandler {
    //TODO need to handle exceptions
    private String server;
    private String db;
    private Connection connection;
    private Statement statement;

    public SQLHandler() {}

    public SQLHandler(String server_, String db_) throws SQLException {
        connectToServer(server_, db_);
    }

    /**
     * Connect the handler to a provided server and database
     *
     * @param server_ Name of server to connect to
     * @param db_ Name of database of {@code server_} to connect to
     *
     * @throws SQLException
     */
    public final void connectToServer(String server_, String db_) throws SQLException {
        server = server_;
        db = db_;
        String connectionUrl = "jdbc:sqlserver://"
                + "%s;".formatted(server_)
                + "databaseName=%s;".formatted(db_)
                + "trustServerCertificate=true;"
                + "integratedSecurity=true;";
        this.connection = DriverManager.getConnection(connectionUrl);
        this.statement = connection.createStatement();
    }


    /**
     * Queries the db with the provided string and returns a map of the results.The map
     * is keyed to the column name, with each element of that column representing a row
     *
     * ex. Map['Name'][0] would return the name of the first row
     *
     * @see {@code SQLTable}
     *
     * @param queryStr
     * @return Map of all the data
     */
    public SQLTable executeQuery(String queryStr){
        SQLTable tbl;
        try {
            ResultSet rs = statement.executeQuery(queryStr);
            ResultSetMetaData rsmd = rs.getMetaData();

            int colCnt = rsmd.getColumnCount();

            tbl = new SQLTable(new ArrayList<>(){{
                for (int i = 1; i <= colCnt; i++) {
                    add(rsmd.getColumnLabel(i));
                }
            }});

            while ( rs.next() ) {
                ArrayList<String> data = new ArrayList<>() {
                    {
                        for (int i = 1; i <= colCnt; i++) {
                            add(rs.getString(i));
                        }
                    }
                };
                tbl.insertRow(data);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            tbl = null;
        }

        return tbl;
    }

    /**
     * Update the {@code db} with the provided update string
     * @param updateStr
     * @return
     */
    public boolean executeUpdate(String updateStr) {
        try {
            statement.executeUpdate(updateStr);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public String getServer() {
        return server;
    }

    public String getDB() {
        return db;
    }
}
