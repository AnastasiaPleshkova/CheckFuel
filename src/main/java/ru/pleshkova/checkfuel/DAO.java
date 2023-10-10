package ru.pleshkova.checkfuel;

import java.io.BufferedReader;
import java.io.FileInputStream;
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
import java.util.Properties;

public class DAO {
    private static Connection connection;

    static {
        Properties properties = new Properties();
        Path filePath = Paths.get("src/main/resources/ru/pleshkova/checkfuel/bd/database.properties");

        try {
            properties.load(Files.newBufferedReader(filePath));

            // Считываем значения из файла и устанавливаем соединение
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
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
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM archive_records");
                List<Record> result = new ArrayList<>();
                while (resultSet.next()) {
                    int km = resultSet.getInt(2);
                    Date date = resultSet.getDate(3);
                    Double litres = resultSet.getDouble(4);
                    Double kmOnLitresBK = resultSet.getDouble(5);
                    Double kmOnLitresREAL = resultSet.getDouble(6);

                    result.add(new Record(date, km, litres, kmOnLitresBK, kmOnLitresREAL));
                }
                return result;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return new ArrayList<>();
        }
    }
