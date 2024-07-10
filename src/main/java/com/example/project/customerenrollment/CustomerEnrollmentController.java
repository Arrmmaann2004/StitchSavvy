package com.example.project.customerenrollment;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import static com.example.project.alert.showMymsg;
import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class CustomerEnrollmentController {

    Connection con;
    PreparedStatement stmt;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtCustomerAddress;

    @FXML
    private TextField txtCustomerCity;

    @FXML
    private DatePicker txtCustomerDate;

    String []gender={"Male","Female","Prefer Not to Say"};

    @FXML
    private ComboBox<String> txtCustomerGender;

    @FXML
    private TextField txtCustomerMobile;

    @FXML
    private TextField txtCustomerName;


    @FXML
    void doClear(ActionEvent event) {
        txtCustomerName.setText("");
        txtCustomerMobile.setText("");
        txtCustomerAddress.setText("");
        txtCustomerCity.setText("");
        txtCustomerDate.setValue(null);
        txtCustomerGender.getEditor().setText("");
    }

    @FXML
    void doFetch(ActionEvent event) {
        try {
            stmt = con.prepareStatement("select * from customerEnroll where mobile = ?");
            stmt.setString(1,txtCustomerMobile.getText());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                txtCustomerName.setText(rs.getString("cname"));
                txtCustomerAddress.setText(rs.getString("address"));
                txtCustomerCity.setText(rs.getString("city"));
                txtCustomerGender.getSelectionModel().select(rs.getString("gender"));
                Date db = rs.getDate("dob");
                txtCustomerDate.setValue(((java.sql.Date)db).toLocalDate());
            }
            else{
                showMymsg("This Mobile Number doesn't exist");
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void doSave(ActionEvent event) throws Exception {

        try{
            stmt = con.prepareStatement("select * from customerEnroll where mobile = ?");
            stmt.setString(1,txtCustomerMobile.getText());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                showMymsg("This Number Already Exists");
                return;
            }
            stmt = con.prepareStatement("insert into customerEnroll values(?,?,?,?,?,?,current_date())");
            stmt.setString(1,txtCustomerMobile.getText());
            stmt.setString(2,txtCustomerName.getText());
            stmt.setString(3,txtCustomerAddress.getText());
            stmt.setString(4,txtCustomerCity.getText());
            stmt.setString(5, txtCustomerGender.getSelectionModel().getSelectedItem());
            LocalDate dt = txtCustomerDate.getValue();
            java.sql.Date date = java.sql.Date.valueOf(dt);
            stmt.setDate(6,date);
            stmt.executeUpdate();
            showMymsg("Data Added Successfully");
        }
        catch(Exception exp){
            exp.printStackTrace();
        }

    }

    @FXML
    void doUpdate(ActionEvent event) {
        try{
            stmt = con.prepareStatement("update customerEnroll set cname=? , address=? , city=? , gender=? , dob=? where mobile=?");
            stmt.setString(6,txtCustomerMobile.getText());
            stmt.setString(1,txtCustomerName.getText());
            stmt.setString(2,txtCustomerAddress.getText());
            stmt.setString(3,txtCustomerCity.getText());
            stmt.setString(4, txtCustomerGender.getSelectionModel().getSelectedItem());
            LocalDate dt = txtCustomerDate.getValue();
            java.sql.Date date = java.sql.Date.valueOf(dt);
            stmt.setDate(5,date);
            stmt.executeUpdate();
            showMymsg("Data Updated Successfully");
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        txtCustomerGender.getItems().addAll(gender);
        con = doConnect();
        if(con==null)
            System.out.println("Connection not established");
        else
            System.out.println("Connection Done");
    }

}
