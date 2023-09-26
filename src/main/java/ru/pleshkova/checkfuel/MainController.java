/**
 * Sample Skeleton for 'main-view.fxml' Controller Class
 */

package ru.pleshkova.checkfuel;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
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
    Record calculatedRecord;

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

        List<Record> records = DAO.getRecordsFromBD();
        Record lastRecord = Record.getLastRecord(records);
        Integer oldKM = lastRecord.getKm();
        oldKmLabel.setText(String.valueOf(oldKM));

        Integer tekKMInt;
        try {
        String tekKMText = nowKmData.getText();
        tekKMInt = Integer.parseInt(tekKMText);
        }
        catch (NumberFormatException e){
            errorLabel.setVisible(true);
            errorLabel.setText("Не смог считать текущий пробег. Введите целое число");
            return;
        }
        if (oldKM>tekKMInt) {
            errorLabel.setVisible(true);
            errorLabel.setText(String.format("Значение текущего пробега не может быть меньше исторического - %d", oldKM));
            return;
        }
        int passesKM = tekKMInt-oldKM;
        realKmData.setText(String.valueOf(passesKM));

        Double litres;
        try {
            litres = Double.valueOf(nowLitrData.getText());
        } catch (NumberFormatException e){
            errorLabel.setVisible(true);
            errorLabel.setText("Не смог считать сколько залито литров. Введите число с точкой");
            return;
        }
        String kmOnLitresString = String.format(Locale.ENGLISH,"%.2f",litres*100/(tekKMInt-oldKM));
        realKmOnLitresData.setText(kmOnLitresString);
        realKmData.setVisible(true);
        realKmOnLitresData.setVisible(true);
        canSave = true;

        String kmOnLitresBKString = enteredCompData.getText();

        if (kmOnLitresBKString == null || kmOnLitresBKString.isEmpty()) {
            calculatedRecord = new Record(new Date(LocalDate.now().getYear() - 1900, LocalDate.now().getMonthValue() - 1,
                    LocalDate.now().getDayOfMonth()), tekKMInt, litres, Double.valueOf(kmOnLitresString));
        } else {
            calculatedRecord = new Record(new Date(LocalDate.now().getYear() - 1900, LocalDate.now().getMonthValue() - 1,
                    LocalDate.now().getDayOfMonth()), tekKMInt, litres, Double.valueOf(kmOnLitresBKString),
                    Double.valueOf(kmOnLitresString));
        }

    }

    @FXML
    void saveToArсhive(ActionEvent event) {
        errorLabel.setVisible(true);

        if (canSave) {
            try {
                if ((calculatedRecord.getKm() == Integer.parseInt(nowKmData.getText())) &&
                        (Math.abs(calculatedRecord.getLitres() - Double.valueOf(nowLitrData.getText())) < 0.01)) {
                    if (new DAO().addedRecord(calculatedRecord)) {
                        errorLabel.setText("Save successful");
                    } else {
                        errorLabel.setText("Save not successful");
                    }
                } else {
                    errorLabel.setText("Кажется изменились данные. Проведите заново расчет");
                    return;
                }
            }
            catch (Exception e){
                errorLabel.setVisible(true);
                errorLabel.setText("Something wrong");
                e.printStackTrace();
                return;
            }
        } else {
            errorLabel.setText("Рассчитайте данные для сохранения");
            return;
        }

        oldKmLabel.setText("-");
        nowKmData.setText("");
        nowLitrData.setText("");
        enteredCompData.setText("");
        realKmData.setVisible(false);
        realKmOnLitresData.setVisible(false);
        canSave = false;
        calculatedRecord = null;
    }

    @FXML
    void showArсhive(ActionEvent event) {
        errorLabel.setVisible(false);
        arhiveShowArea.setVisible(true);
        StringBuilder sb = new StringBuilder();
        for (Record record : DAO.getRecordsFromBD()){
            sb.append(record.toLine());
            sb.append("\n");
        }
        arhiveShowArea.setText(sb.toString());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        arhiveShowArea.setWrapText(true);
        errorLabel.setWrapText(true); // включить перенос строки
        oldKmLabel.setText("-");
    }

}
