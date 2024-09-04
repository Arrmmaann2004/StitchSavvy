package com.example.project.workerconsole;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import static com.example.project.alert.showMymsg;
import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class WorkerConsoleController {


    String[] clothingTypes = {
            "Shirt",
            "Pants",
            "Suit",
            "Blouse",
            "Skirt",
            "Dress",
            "Coat",
            "Jacket",
            "Vest",
            "Jeans",
            "Shorts",
            "Overcoat",
            "Waistcoat",
            "Tuxedo",
            "Saari",
            "Shervani",
            "Kurta"
    };

    Connection con;
    PreparedStatement stmt;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> listWorkerSpecialization;

    @FXML
    private TextField txtWorkerAddress;

    @FXML
    private TextField txtWorkerMobile;

    @FXML
    private TextField txtWorkerName;

    @FXML
    private TextField txtWorkerSpecialization;

    String spl="";

    @FXML
    void doAddSpecialization(MouseEvent event) {

        if(event.getClickCount()==2){
            spl = spl + listWorkerSpecialization.getSelectionModel().getSelectedItem() + ";";
        }
        txtWorkerSpecialization.setText(spl);
    }

    @FXML
    void doClear(ActionEvent event) {
        txtWorkerAddress.setText("");
        txtWorkerMobile.setText("");
        txtWorkerName.setText("");
        txtWorkerSpecialization.setText("");
    }

    @FXML
    void doSave(ActionEvent event) {
        try{
            stmt = con.prepareStatement("insert into workers values(?,?,?,?)");
            stmt.setString(1,txtWorkerName.getText());
            stmt.setString(2,txtWorkerAddress.getText());
            stmt.setString(3,txtWorkerMobile.getText());
            stmt.setString(4,txtWorkerSpecialization.getText());
            stmt.executeUpdate();
             spl = "";
            showMymsg("Data Added");
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        listWorkerSpecialization.getItems().addAll(clothingTypes);
        con = doConnect();
        if(con==null){
            System.out.println("Conection didn't established");
        }
        else {
            System.out.println("congo connection established");
        }
    }

}
