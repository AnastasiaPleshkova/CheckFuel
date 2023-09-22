/**
 * Sample Skeleton for 'main-view.fxml' Controller Class
 */

package ru.pleshkova.checkfuel;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MainController {

    String archivePath = "src/main/resources/ru/pleshkova/checkfuel/archive.txt";
    Boolean canSave = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="arhiveShowArea"
    private TextArea arhiveShowArea; // Value injected by FXMLLoader
    @FXML // fx:id="calculateButton"
    private Button calculateButton; // Value injected by FXMLLoader
    @FXML // fx:id="enteredCompData"
    private TextField enteredCompData; // Value injected by FXMLLoader
    @FXML // fx:id="errorLabel"
    private Label errorLabel; // Value injected by FXMLLoader
    @FXML // fx:id="nowKmData"
    private TextField nowKmData; // Value injected by FXMLLoader
    @FXML // fx:id="nowLitrData"
    private TextField nowLitrData; // Value injected by FXMLLoader
    @FXML // fx:id="oldKmLabel"
    private Label oldKmLabel; // Value injected by FXMLLoader
    @FXML // fx:id="realKmData"
    private Label realKmData; // Value injected by FXMLLoader
    @FXML // fx:id="realKmOnLitresData"
    private Label realKmOnLitresData; // Value injected by FXMLLoader
    @FXML // fx:id="saveButton"
    private Button saveButton; // Value injected by FXMLLoader
    @FXML // fx:id="showArhiveButton"
    private Button showArhiveButton; // Value injected by FXMLLoader

    @FXML
    void calculate(MouseEvent event) {

        errorLabel.setVisible(false);
        Integer oldKM = Integer.parseInt(oldKmLabel.getText());
        Integer tekKMInt;
        try{
        String tekKMText = nowKmData.getText();
        tekKMInt = Integer.parseInt(tekKMText);
        }
        catch (NumberFormatException e){
            errorLabel.setVisible(true);
            errorLabel.setText("Не смог считать текущий пробег. Введите целое число");
            return;
        }
        if (oldKM>tekKMInt){
            errorLabel.setVisible(true);
            errorLabel.setText("Значение текущего пробега не может быть меньше исторического");
            return;
        }
        realKmData.setText(String.valueOf(tekKMInt-oldKM));

        Double litres;
        try{litres = Double.valueOf(nowLitrData.getText());}
        catch (NumberFormatException e){
            errorLabel.setVisible(true);
            errorLabel.setText("Не смог считать сколько залито литров. Введите число с точкой");
            return;
        }
        realKmOnLitresData.setText(String.format(Locale.ENGLISH,"%.2f",litres*100/(tekKMInt-oldKM)));
        realKmData.setVisible(true);
        realKmOnLitresData.setVisible(true);
        canSave = true;
    }

    @FXML
    void saveToArсhive(ActionEvent event) {
        errorLabel.setVisible(false);
        arhiveShowArea.setVisible(false);
        if (canSave){
            boolean canSaveToBD = new DAO().addedRecord(new Record(Date.valueOf(LocalDate.now()), Double.valueOf(nowKmData.getText()),
                    Double.valueOf(enteredCompData.getText())));
            try (BufferedWriter rd = new BufferedWriter(new FileWriter(archivePath, true)))
            {
                StringBuilder result = new StringBuilder("\n" + LocalDate.now() + " Пробег:" + nowKmData.getText()+
                    " Реальный расход:" + realKmOnLitresData.getText() + " Расход по БК:" + enteredCompData.getText());
                rd.write(result.toString());
            }
            catch (IOException e){
                errorLabel.setVisible(true);
                errorLabel.setText("Не смог найти файл с архивом. Данные не сохранены");
                return;
            }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Введите вначале данные для сохранения");
            return;
        }

        oldKmLabel.setText(nowKmData.getText());
        nowKmData.setText("");
        nowLitrData.setText("");
        enteredCompData.setText("");
        realKmData.setVisible(false);
        realKmOnLitresData.setVisible(false);
        canSave = false;
    }

    @FXML
    void showArсhive(ActionEvent event) {
        boolean resultRecievingFromDB = new DAO().getRecordsFromDB();

        errorLabel.setVisible(false);
        arhiveShowArea.setVisible(true);

        try (BufferedReader rd = new BufferedReader(new FileReader(archivePath)))
        {
            String line;
            StringBuilder result = new StringBuilder();
            while ((line=rd.readLine())!=null) {
                result.append(line);
                result.append("\n");
            }
            arhiveShowArea.setText(result.toString());

        } catch (IOException e){
            errorLabel.setVisible(true);
            errorLabel.setText("Не найден файл с архивом");
            return;
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        arhiveShowArea.setWrapText(true);
        errorLabel.setWrapText(true); // включить перенос строки
        Integer oldKM;
        try (BufferedReader rd = new BufferedReader(new FileReader(archivePath)))
        {
            String line;
            String prevLine = " ";
            while ((line=rd.readLine())!=null) {
                prevLine = line;
            }

            String temp = prevLine.split(":")[1];
            oldKM = Integer.parseInt(temp.split(" ")[0]);

        } catch (Exception e){
            oldKM = 0;
        }

        oldKmLabel.setText(String.valueOf(oldKM));

    }

}
