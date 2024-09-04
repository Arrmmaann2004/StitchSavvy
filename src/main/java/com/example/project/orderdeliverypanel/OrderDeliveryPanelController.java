package com.example.project.orderdeliverypanel;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class OrderDeliveryPanelController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private TextField txtTotalBill;

    @FXML
    private ListView<String> listBill;

    @FXML
    private ListView<String> listItemDress;

    @FXML
    private ListView<String> listOrderId;

    @FXML
    private ListView<String> listStatus;

    @FXML
    private TextField txtMobile;

    @FXML
    void doDeliver(ActionEvent event) {
        try {
            stmt = con.prepareStatement("update measurements set bill=0 , status=3 where mobile = ? and status=2");
            stmt.setString(1,txtMobile.getText());
            stmt.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        txtTotalBill.setText("0");
    }

    @FXML
    void doFetch(ActionEvent event) {
        listStatus.getItems().clear();
        listBill.getItems().clear();
        listOrderId.getItems().clear();
        listItemDress.getItems().clear();
        int bill = 0;
        try{
            stmt = con.prepareStatement("select * from measurements where mobile = ? and status in (1,2)");
            stmt.setString(1,txtMobile.getText());
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                if(String.valueOf(res.getInt("status")).equals("2"))
                    bill+= Integer.parseInt(res.getString("bill"));
                listBill.getItems().add(String.valueOf(res.getInt("bill")));
                listItemDress.getItems().add(res.getString("dress"));
                listOrderId.getItems().add(String.valueOf(res.getInt("orderid")));
                listStatus.getItems().add(String.valueOf(res.getInt("status")));

            }
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
        txtTotalBill.setText(String.valueOf(bill));
    }
    Connection con;
    PreparedStatement stmt;

    @FXML
    void initialize() {
         con = doConnect();
         if(con==null){
             System.out.println("Sorryy");
         }
         else
             System.out.println("Congrats");
    }

}
