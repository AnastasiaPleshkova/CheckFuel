package ru.pleshkova.checkfuel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAO {


        // JDBC URL, username and password of MySQL server
        private static final String url = "jdbc:mysql://localhost:3306/checkfuelchema";
        private static final String user = "root";
        private static final String password = "nopasswordhere";

        // JDBC variables for opening and managing connection
        private static Connection connection;
        private static Statement stmt;
        private static ResultSet rs;

        public static ResultSet connectionToBD(String query) {

            try {
                connection = DriverManager.getConnection(url, user, password); // открываем подключение к БД
                stmt = connection.createStatement(); // создаем объект для совершения запросов
                rs = stmt.executeQuery(query);
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
            return rs;
        }

        public static void closeConnectionToBD(){
            try { connection.close(); } catch (SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch (SQLException se) { /*can't do anything */ }
            try { rs.close();} catch (SQLException se) { }
        }

        public boolean addedRecord(Record record) {

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT archive_records(currentKM, date, litres, kmonlitresBK, kmonlitresREAL) VALUES(");
            sb.append(record.getDate());
            sb.append(")");

            rs = connectionToBD(sb.toString());
            closeConnectionToBD();
            return false;

        }

        public static List<Record> getRecordsFromBD() {
            try {
                ResultSet resultSet = connectionToBD("SELECT * FROM archive_records");
                List<Record> result = new ArrayList<>();
                while (rs.next()) {
                    int km = rs.getInt(2);
                    Date date = rs.getDate(3);
                    Double litres = rs.getDouble(4);
                    Double kmOnLitresBK = rs.getDouble(5);
                    Double kmOnLitresREAL = rs.getDouble(6);

                    result.add(new Record(date, km, litres, kmOnLitresBK, kmOnLitresREAL));
                    //System.out.print(result);
                }
                return result;
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeConnectionToBD();
            }

            return new ArrayList<>();
        }
    }
