package com.example.project.workertableview;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.example.project.alert.showMymsg;
import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class WorkerTableViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    String[] dresses = {
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

    @FXML
    private ComboBox<String> comboDress;

    @FXML
    private TableView<ProfileBean> tblview;

    @FXML
    void doExport(ActionEvent event){
        ObservableList<ProfileBean> getRecords = tblview.getItems();
        doExportToExcel(getRecords);
    }

    void doExportToExcel(ObservableList<ProfileBean> records) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Input Excel File Name");
        dialog.setContentText("Enter the Excel file name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String fileName = result.get().trim();
            if (fileName.isEmpty()) {
                showMymsg("Invalid File Name");
            } else {
                try {
                    writeExcel(fileName, records);
                    showMymsg("Exported to Excel!");
                    System.out.println("Exported");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            showMymsg("File name input was cancelled.");
        }
    }

    public void writeExcel(String fileName, ObservableList<ProfileBean> records) throws Exception {
        Writer writer = null;
        try {
            File file = new File(fileName + ".csv");
            writer = new BufferedWriter(new FileWriter(file));
            String text = "Name,Mobile,Address,Specialization\n";
            writer.write(text);
            for (ProfileBean p : records) {
                text = p.getWname() + "," + p.getMobile() + "," + p.getAddress() + "," + p.getSplz() + "\n";
                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @FXML
    void doShowAll(ActionEvent event) {
        comboDress.getSelectionModel().clearSelection();
        tblview.getColumns().clear();

        TableColumn<ProfileBean, String> name = new TableColumn<>("Worker Name");
        name.setCellValueFactory(new PropertyValueFactory<>("wname"));
        name.setMinWidth(100);

        TableColumn<ProfileBean, String> address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        address.setMinWidth(100);

        TableColumn<ProfileBean, String> mobile = new TableColumn<>("Contact");
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobile.setMinWidth(100);

        TableColumn<ProfileBean, String> splz = new TableColumn<>("Specialisations");
        splz.setCellValueFactory(new PropertyValueFactory<>("splz"));
        splz.setMinWidth(100);

        tblview.getColumns().addAll(name, address, mobile, splz);
        ObservableList<ProfileBean> records = getAllRecords();
        tblview.setItems(records);
    }

    Connection con;
    PreparedStatement stmt;

    ObservableList<ProfileBean> getAllRecords(){
        ObservableList<ProfileBean>records = FXCollections.observableArrayList();

        try{
            stmt = con.prepareStatement("select * from workers");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String wname = res.getString("wname");
                String address = res.getString("address");
                String mobile = res.getString("mobile");
                String splz = res.getString("splz");
                records.add(new ProfileBean(wname,address,mobile,splz));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return records;
    }

    @FXML
    void doChange(ActionEvent event) {
        tblview.getColumns().clear();

        TableColumn<ProfileBean, String> name = new TableColumn<>("Worker Name");
        name.setCellValueFactory(new PropertyValueFactory<>("wname"));
        name.setMinWidth(100);

        TableColumn<ProfileBean, String> address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        address.setMinWidth(100);

        TableColumn<ProfileBean, String> mobile = new TableColumn<>("Contact");
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobile.setMinWidth(100);

        tblview.getColumns().addAll(name, address, mobile);
        ObservableList<ProfileBean> records = getSomeRecords();
        tblview.setItems(records);
    }

    ObservableList<ProfileBean> getSomeRecords(){
        ObservableList<ProfileBean>records = FXCollections.observableArrayList();
        String drs = comboDress.getSelectionModel().getSelectedItem();
        try{
            stmt = con.prepareStatement("select wname , address , mobile from workers where splz like ?");
            stmt.setString(1,"%"+drs+"%");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String wname = res.getString("wname");
                String address = res.getString("address");
                String mobile = res.getString("mobile");
                ProfileBean pb = new ProfileBean();
                pb.setWname(wname);
                pb.setAddress(address);
                pb.setMobile(mobile);
                records.add(pb);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return records;
    }

    @FXML
    void initialize() {
        con = doConnect();
        if(con==null){
            System.out.println("Connection not established");
        }
        else{
            System.out.println("Connection Established");
        }
        comboDress.getItems().addAll(dresses);
    }
}
