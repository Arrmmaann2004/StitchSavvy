package com.example.project;

import javafx.scene.control.Alert;

public class alert {
    public static void showMymsg(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Record Data");
        alert.showAndWait();
    }
}
