package com.example.project.readyproducts;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class ReadyProductsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> comboWorkers;

    @FXML
    private ListView<String> listDoDelReadyProducts;

    @FXML
    private ListView<String> listDressReadyProducts;

    @FXML
    private ListView<String> listOrderId;

    @FXML
    void doAdd(ActionEvent event) {
//        orderid , dress , date of delivery
        String worker = comboWorkers.getSelectionModel().getSelectedItem();
        try {
            stmt = con.prepareStatement("select orderid , dress , dodel from measurements where wname = ? and status = 1");
            stmt.setString(1,worker);
            ResultSet res = stmt.executeQuery();
            listOrderId.getItems().clear();
            listDressReadyProducts.getItems().clear();
            listDoDelReadyProducts.getItems().clear();
            while(res.next()){
                listOrderId.getItems().add(res.getString("orderid"));
                listDressReadyProducts.getItems().add(res.getString("dress"));
                Date ddl = res.getDate("dodel");
                listDoDelReadyProducts.getItems().add(String.valueOf(((Date)ddl).toLocalDate()));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void doRefresh(MouseEvent event) throws SQLException {
        initialize();
    }

    @FXML
    void doRemove(MouseEvent event) {
        if(event.getClickCount()==2){
            String ordid = listOrderId.getSelectionModel().getSelectedItem();
            int index = listOrderId.getSelectionModel().getSelectedIndex();
            try{
                stmt = con.prepareStatement("update measurements set status = 2 where orderid = ?");
                stmt.setString(1,ordid);
                stmt.executeUpdate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            listOrderId.getItems().remove(index);
            listDressReadyProducts.getItems().remove(index);
            listDoDelReadyProducts.getItems().remove(index);
        }
    }

    @FXML
    void doRemoveAll(ActionEvent event) {
//        int index = listOrderId.getSelectionModel().getSelectedIndex();
        try{
            for(String ordid : listOrderId.getItems()){
                stmt = con.prepareStatement("update measurements set status = 2 where orderid = ?");
                stmt.setString(1,ordid);
                stmt.executeUpdate();
            }
            listOrderId.getItems().clear();
            listDressReadyProducts.getItems().clear();
            listDoDelReadyProducts.getItems().clear();

        }
        catch (Exception exp){
            exp.printStackTrace();
        }

    }

    Connection con=null;
    PreparedStatement stmt;

    @FXML
    void initialize() throws SQLException {
        con = doConnect();
        if(con==null){
            System.out.println("Connection Not established");
        }
        else {
            System.out.println("Connection Done");
        }
       try{
           stmt = con.prepareStatement("select distinct wname from measurements where status = 1");
           ResultSet res = stmt.executeQuery();
           comboWorkers.getItems().clear();
           while(res.next()){
               comboWorkers.getItems().add(res.getString("wname"));
           }
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }

}
