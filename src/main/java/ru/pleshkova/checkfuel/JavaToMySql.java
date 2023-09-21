package ru.pleshkova.checkfuel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JavaToMySql {


        // JDBC URL, username and password of MySQL server
        private static final String url = "jdbc:mysql://localhost:3306/checkfuelchema";
        private static final String user = "root";
        private static final String password = "nopasswordhere";

        // JDBC variables for opening and managing connection
        private static Connection con;
        private static Statement stmt;
        private static ResultSet rs;

        public static void connectionToBD() {
            try{
                Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                System.out.println("Connection succesfull!");
            }
            catch(Exception ex){
                System.out.println("Connection failed...");

                System.out.println(ex);
            }

            String query = "select * from archive_records";

            try {
                // opening database connection to MySQL server
                con = DriverManager.getConnection(url, user, password);

                // getting Statement object to execute query
                stmt = con.createStatement();

                // executing SELECT query
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    //int count = rs.getInt(1);
                    System.out.println(rs.getString(1));
                }

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            } finally {
                //close connection ,stmt and resultset here
                try { con.close(); } catch(SQLException se) { /*can't do anything */ }
                try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
                try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
            }
        }

    }
