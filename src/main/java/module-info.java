module ru.pleshkova.checkfuel {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.pleshkova.checkfuel to javafx.fxml;
    exports ru.pleshkova.checkfuel;
}