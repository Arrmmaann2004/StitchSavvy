package com.example.project.measurements;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import static com.example.project.alert.showMymsg;
import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class MeasurementsController {

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
    private ComboBox<String> comboDress;

    @FXML
    private ComboBox<String> comboWorkers;

    @FXML
    private AnchorPane measurementsForm;

    @FXML
    private ImageView imageview;

    @FXML
    private TextField txtBill;

    @FXML
    private TextField txtCustomerMobile;

    @FXML
    private DatePicker txtDateOfDelivery;

    @FXML
    private TextArea txtMeasurementsOthers;

    @FXML
    private TextField txtPricePerUnit;

    @FXML
    private TextField txtQuantity;

    @FXML
    void doFindExisting(ActionEvent event) {
        String cloth = comboDress.getSelectionModel().getSelectedItem();
        try{
            stmt = con.prepareStatement("select measurement from measurements where dress = ?");
            stmt.setString(1,cloth);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                String msrmnt = res.getString("measurement");
                txtMeasurementsOthers.setText(msrmnt);
            }
        }
        catch(Exception exp){
            exp.printStackTrace();
        }

    }

    @FXML
    void doAddWorker(){
        try{
            stmt = con.prepareStatement("select wname from workers where splz like ?");
            String cloth = comboDress.getSelectionModel().getSelectedItem();
            stmt.setString(1,"%"+cloth+"%");
            ResultSet res = stmt.executeQuery();
            comboWorkers.getItems().clear();
            while (res.next()) {
                comboWorkers.getItems().add(res.getString("wname"));
            }
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }

    String []clr = {""};

    @FXML
    void doClear(ActionEvent event) {
        txtCustomerMobile.setText("");
        comboDress.setValue("");
        txtDateOfDelivery.setValue(null);
        txtQuantity.setText("");
        txtPricePerUnit.setText("");
        txtBill.setText("");
        txtMeasurementsOthers.setText("");
        comboWorkers.getItems().addAll(clr);
        imageview.setImage(null);

    }

    @FXML
    void doClose(ActionEvent event) {
        Stage stage = (Stage)measurementsForm.getScene().getWindow();
        stage.close();
    }

    String selectedImagePath = "";
    int bill;

    int orderId;

    @FXML
    void doSearch(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Search");
        dialog.setContentText("Fill the OrderId");
        Optional<String>result =dialog.showAndWait();
        if(result.isPresent()){
            if(result.equals("")){
                showMymsg("Invalid OrderId");
            }
            else{
                orderId = Integer.parseInt(result.get());
            }
        }
        else{
            showMymsg("Don't Have OrderId");
        }

        try{
            stmt = con.prepareStatement("select * from measurements where orderid=?");
            stmt.setString(1,String.valueOf(orderId));
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                txtCustomerMobile.setText(res.getString("mobile"));
                comboDress.setValue(res.getString("dress"));
                Date ddl = res.getDate("dodel");
                txtDateOfDelivery.setValue(((java.sql.Date)ddl).toLocalDate());
                txtQuantity.setText(res.getString("qty"));
                int ppu = Integer.parseInt(res.getString("bill"))/Integer.parseInt(res.getString("qty"));
                txtPricePerUnit.setText(String.valueOf(ppu));
                txtBill.setText(res.getString("bill"));
                txtMeasurementsOthers.setText(res.getString("measurement"));
                comboWorkers.getSelectionModel().select(res.getString("wname"));
                String ppic = res.getString("pic");
                if (ppic != null && !ppic.isEmpty()) {
                    try{
                        Image image = new Image(new FileInputStream(ppic));
                        imageview.setImage(image);
                    }
                    catch (Exception exp){
                        exp.printStackTrace();
                    }
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void doSave(ActionEvent event) {
        try{
            stmt = con.prepareStatement("insert into measurements values(null,?,?,?,?,?,?,?,?,current_date(),1)",Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,txtCustomerMobile.getText());
            stmt.setString(2,comboDress.getSelectionModel().getSelectedItem());
            stmt.setString(3,selectedImagePath);
            LocalDate dt =txtDateOfDelivery.getValue();
            java.sql.Date ddl = java.sql.Date.valueOf(dt);
            stmt.setDate(4,ddl);
            stmt.setInt(5,Integer.parseInt(txtQuantity.getText()));

            txtBill.setText(String.valueOf(bill));
            stmt.setInt(6,bill);
            stmt.setString(7,txtMeasurementsOthers.getText());
            stmt.setString(8,comboWorkers.getSelectionModel().getSelectedItem());
            int rowsaffected = stmt.executeUpdate();
            if(rowsaffected>0){
                try {
                    ResultSet res = stmt.getGeneratedKeys();
                    if (res.next()){
                        long autoGeneratedId = res.getLong(1);
//                        System.out.println("Auto Generated ID: " + autoGeneratedId);
                        showMymsg("OrderId : "+autoGeneratedId);
                    }
                }
                catch (Exception exp){
                    exp.printStackTrace();
                }
            }

        }
        catch(Exception exp){
            exp.printStackTrace();
        }


    }

    @FXML
    void doUpdate(ActionEvent event) {
        try{
            stmt = con.prepareStatement("update measurements set mobile=? , dress=? , pic=? , dodel=? , qty=? , bill=? , measurement=? , wname=? where orderid=?");
            stmt.setString(1,txtCustomerMobile.getText());
            stmt.setString(2,comboDress.getSelectionModel().getSelectedItem());
            stmt.setString(3,selectedImagePath);
            LocalDate dt =txtDateOfDelivery.getValue();
            if(dt!=null){
                java.sql.Date ddl = java.sql.Date.valueOf(dt);
                stmt.setDate(4,ddl);
            }
            stmt.setInt(5,Integer.parseInt(txtQuantity.getText()));

            txtBill.setText(String.valueOf(bill));
            stmt.setInt(6,bill);
            stmt.setString(7,txtMeasurementsOthers.getText());
            stmt.setString(8,comboWorkers.getSelectionModel().getSelectedItem());
            stmt.setString(9,String.valueOf(orderId));
            int rowsaffected = stmt.executeUpdate();
            if(rowsaffected>0){
               showMymsg("Order ID : "+orderId+" Updated");
            }

        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void doUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images","*.jpg","*.png"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null)
        {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                imageview.setImage(image);
                selectedImagePath = selectedFile.getAbsolutePath();
            }
            catch (Exception exp){
                exp.printStackTrace();
            }
        }
        else
            System.out.println("No file selected");
//        selectedImagePath = selectedFile.toURI().toString();
    }

    @FXML
    void initialize() {
        con = doConnect();
        if(con==null)
            System.out.println("sorry");
        else
            System.out.println("Congo");

        comboDress.getItems().addAll(clothingTypes);
    }


    public void doCalc(javafx.scene.input.MouseEvent mouseEvent) {
        bill=0;
        bill = Integer.parseInt(txtQuantity.getText()) * Integer.parseInt(txtPricePerUnit.getText());
        txtBill.setText(String.valueOf(bill));

    }
}
