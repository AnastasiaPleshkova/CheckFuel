package ru.pleshkova.checkfuel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        private static final Path passwordPath = Paths.get("src/main/resources/ru/pleshkova/checkfuel/bd/password.txt");
        private static final String password = getPassword();

        private static String getPassword() {
            if (Files.exists(passwordPath)) {
                try (BufferedReader rd = Files.newBufferedReader(passwordPath)) {
                    return rd.readLine();
                } catch (IOException ex) {
                    System.out.println("Couldn't read file");
                    ex.printStackTrace();
                }
            } else {
                System.out.println("File with password not found");
            }
            return "";
        }

        // JDBC variables for opening and managing connection
        private static Connection connection;
        private static Statement stmt;
        private static ResultSet rs;

        public static Statement connectToBD() {
            try {
            connection = DriverManager.getConnection(url, user, password); // открываем подключение к БД
            stmt = connection.createStatement(); // создаем объект для совершения запросов
            } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            }
            return stmt;
        }

        public static ResultSet getRSfromBD(String query) {
            try {
                rs = connectToBD().executeQuery(query);
            } catch (SQLException sqlEx) { sqlEx.printStackTrace(); }
            return rs;
        }

        public static void closeConnectionToBD(){
            try { connection.close(); } catch (SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch (SQLException se) { /*can't do anything */ }
            try { rs.close();} catch (SQLException se) { }
        }

        public boolean addedRecord(Record record) {

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO `checkfuelchema`.`archive_records` (`currentKM`, `date`, `litres`, `kmonlitresBK`, `kmonlitresREAL`) VALUES ('");
            sb.append(record.getKm());
            sb.append("', '");
            sb.append(record.getDate());
            System.out.println(record.getDate());
            sb.append("', '");
            sb.append(record.getLitres());
            sb.append("', '");
            sb.append(record.getKmOnLitresBK());
            sb.append("', '");
            sb.append(record.getKmOnLitresREAL());
            sb.append("');");

            System.out.println(sb.toString());

            try {
            connectToBD().executeUpdate(sb.toString());
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            closeConnectionToBD();
            return true;
        }

        public static List<Record> getRecordsFromBD() {
            try {
                ResultSet resultSet = getRSfromBD("SELECT * FROM archive_records");
                List<Record> result = new ArrayList<>();
                while (rs.next()) {
                    int km = rs.getInt(2);
                    Date date = rs.getDate(3);
                    Double litres = rs.getDouble(4);
                    Double kmOnLitresBK = rs.getDouble(5);
                    Double kmOnLitresREAL = rs.getDouble(6);

                    result.add(new Record(date, km, litres, kmOnLitresBK, kmOnLitresREAL));
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
