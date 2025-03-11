package edu.uph.ii;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WypiszOsoby extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String dbURL = "jdbc:derby://localhost:1527/nazwabazy;create=true;user=test;password=test";
    private final String tableName = "osoby";
    // jdbc Connection
    private Connection conn = null;
    private Statement stmt = null;

    @Override
    public void init() throws ServletException {
        super.init();
        connectDB();
        createTable();
        insertOsoba(1, "Michalina", "Nowakowska");
        insertOsoba(2, "Tadeusz", "Kowalski");
        insertOsoba(3, "Radosław", "Czerwiec");
        shutdown();
    }

    private void shutdown() {
        try {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }
        } catch (SQLException sqlExcept) {}
    }

    private void connectDB()
    {
        try {
            Class.forName("org.apache.derby.client.ClientAutoloadedDriver"); //Get a connection
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    private void createTable() {
        try {
            stmt = conn.createStatement();
            stmt.execute("create table "+ tableName +"(id int primary key, imie varchar(20), nazwisko varchar(30) )");
            stmt.close();
        } catch (SQLException sqlExcept) { }
    }

    private void insertOsoba(int id, String imie, String nazwisko) {
        try {
            stmt = conn.createStatement();
            stmt.execute("insert into " + tableName + " values (" + id + ",'" + imie + "','" + nazwisko +"')");
            stmt.close();
        } catch (SQLException sqlExcept) { }
    }

    private void printData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Lista osób</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>ID</th><th>Imię</th><th>Nazwisko</th>");
        connectDB();

        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);

            while(results.next()) {
                int id = results.getInt(1);
                String imie = results.getString(2);
                String nazwisko = results.getString(3);
                out.println("<tr><td>"+id+"</td><td>"+imie+"</td><td>"+nazwisko+"</td ></tr>");
            }

            results.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        shutdown();
        out.println("</tr>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printData(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printData(request, response);
    }
}