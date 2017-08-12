package storage;

import java.lang.reflect.Executable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnector {

    private static MySQLConnector instance;
    private Connection connect;
    private Statement statement;

    public static MySQLConnector getInstance() {
        if (instance == null) {
            instance = new MySQLConnector();
        }
        return instance;
    }

    private void readDataBase() {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://host.moddylp.de/admin_codecamp?"
                            + "user=admin_codecamp&password=codecamp42");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL quer

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultofQuery(String query) {
        try {
            readDataBase();
            return statement
                    .executeQuery("SELECT * FROM Karten");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public void close() {
        try {
            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}