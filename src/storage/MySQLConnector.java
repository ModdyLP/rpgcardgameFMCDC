package storage;

import objects.Card;

import java.lang.reflect.Executable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLConnector {

    private static MySQLConnector instance;

    public static MySQLConnector getInstance() {
        if (instance == null) {
            instance = new MySQLConnector();
        }
        return instance;
    }

    private Statement readDataBase() {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Setup the connection with the DB
            Connection connect =  DriverManager
                    .getConnection("jdbc:mysql://host.moddylp.de/admin_codecamp?"
                            + "user=admin_codecamp&password=codecamp42");

            // Statements allow to issue SQL queries to the database
            return connect.createStatement();
            // Result set get the result of the SQL quer

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getResultofQuery(String query) {
        try {
            //System.out.println(query);
            return readDataBase()
                    .executeQuery(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public void execute(String query) {
        //System.out.println(query);
        try {
            Statement st = readDataBase();
            assert st != null;
            st.execute(query);
            st.getConnection().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void insertMany(ArrayList<Integer> cards, int player) {
        try {
            Statement st = readDataBase();
            for (int cardid: cards) {
                st.execute("INSERT INTO `cardtoplayer` (`id`, `cardid`, `playerid`) VALUES (NULL, '" + cardid + "', '" + player + "');");
            }
            st.getConnection().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void close(ResultSet rs) {
        try {
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}