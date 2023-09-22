package ru.pleshkova.checkfuel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {


        // JDBC URL, username and password of MySQL server
        private static final String url = "jdbc:mysql://localhost:3306/checkfuelchema";
        private static final String user = "root";
        private static final String password = "nopasswordhere";

        // JDBC variables for opening and managing connection
        private static Connection connection;
        private static Statement stmt;
        private static ResultSet rs;

        public ResultSet connectionToBD(String query) {

            try {
                connection = DriverManager.getConnection(url, user, password); // открываем подключение к БД
                stmt = connection.createStatement(); // создаем объект для совершения запросов
                rs = stmt.executeQuery(query);
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
            return rs;
        }

        public void closeConnectionToBD(){
            try { connection.close(); } catch (SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch (SQLException se) { /*can't do anything */ }
            try { rs.close();} catch (SQLException se) { }
        }

        public boolean addedRecord(Record record) {

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT archive_records(currentKM, date, litres, kmonlitresBK, kmonlitresREAL) VALUES(");
            sb.append(record.getDate());
            sb.append(")");
            DAO ss = new DAO();
            rs = ss.connectionToBD(sb.toString());
            ss.closeConnectionToBD();
            return false;

        }

        public boolean getRecordsFromDB() {
            DAO dao = new DAO();
            try {
                ResultSet resultSet = dao.connectionToBD("SELECT * FROM archive_records");
                while (rs.next()) {
                    String curKM = rs.getString(2);
                    String date = rs.getString(3);
                    String litres = rs.getString(4);
                    String kmOnLitresBK = rs.getString(5);
                    String kmOnLitresREAL = rs.getString(6);
                    System.out.printf("Дата %s. Пройдено %s км, использовано %s литров. " +
                            "Расход по БК - %s, расход реальный - %s \n", date, curKM, litres, kmOnLitresBK, kmOnLitresREAL);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                dao.closeConnectionToBD();
            }

            return false;
        }
    }
