package edu.uph.ii;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class CreateDB
 */
public class CreateDB {
    private static final String dbURL = "jdbc:derby://localhost:1527/nazwabazy1;create=true;user=test;password=test";
    private static final String tableName = "osoby";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;

    public CreateDB() {
        super();
    }

    private static void connectDB() {
        try {
            //Class.forName("org.apache.derby.jdbc.ClientDriver"); //Get a connection
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
    private static void createTable() {
        try {
            stmt = conn.createStatement();
            stmt.execute("create table "+ tableName +"(id int primary key, imie varchar(20), nazwisko varchar(30) )");
            stmt.close();
        } catch (SQLException sqlExcept) {
            System.err.println(sqlExcept.getMessage());
        }
    }
    private static void insertOsoba(int id, String imie, String nazwisko) {
        try {
            stmt = conn.createStatement();
            stmt.execute("insert into " + tableName + " values (" + id + ",'" + imie + "','" + nazwisko +"')");
            stmt.close();
        } catch (SQLException sqlExcept) {
            System.err.println(sqlExcept.getMessage());
        }
    }
    private static void selectOsoby() {
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for
            (int i=1; i<=numberCols; i++) {
//print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");
            }
            System.out.println("\n--------------------------------------");
            while(results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
                String cityName = results.getString(3);
                System.out.println(id + "\t\t" + restName + "\t\t" +
                        cityName);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            System.err.println(sqlExcept.getMessage());
        }
    }
    private static void shutdown() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }
        } catch (SQLException sqlExcept) {
        }
    }
    public static void main(String[] args){
        connectDB();
        createTable();
        insertOsoba(1, "Michalina", "Nosowska");
        insertOsoba(2, "Jarosław", "Kowalski");
        insertOsoba(3, "Aleksander", "Głowacki");
        insertOsoba(4, "Mariusz", "Kozak");
        selectOsoby();
        shutdown();
    }
}
