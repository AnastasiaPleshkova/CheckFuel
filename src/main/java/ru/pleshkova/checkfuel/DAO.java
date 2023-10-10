package ru.pleshkova.checkfuel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
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

        public boolean addedRecord(Record record) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `checkfuelchema`.`archive_records` " +
                        "(`currentKM`, `date`, `litres`, `kmonlitresBK`, `kmonlitresREAL`) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setInt(1,record.getKm());
                preparedStatement.setDate(2, new java.sql.Date(record.getDate().getTime()));
                preparedStatement.setDouble(3,record.getLitres());
                preparedStatement.setDouble(4, record.getKmOnLitresBK());
                preparedStatement.setDouble(5, record.getKmOnLitresREAL());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            }
            return new ArrayList<>();
        }
    }
