package com.example.project.admindashboard;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.project.HelloApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.example.project.alert.showMymsg;
import static com.example.project.sqlConnector.mySqlConnector.doConnect;

public class AdminDashBoardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane adminpanel;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtProcessOrders;

    @FXML
    private TextField txtTotalOrdersDone;

    @FXML
    private TextField txtWorkerAvail;

    @FXML
    private Button idOpenGetReadyProducts;

    @FXML
    private Button idOpenCustomerEnrollment;

    @FXML
    private TextField txtpendingorders;

    @FXML
    private Button idOpenItemRecords;

    @FXML
    private Button idOpenMeasurements;

    @FXML
    private Button idOpenOrderDeliveryPanel;

    @FXML
    private Button idOpenWorkerConsole;

    @FXML
    private Button idOpenWorkerTable;

    private int incorrectAttempts = 0;

    @FXML
    void doEnableAllButtons(MouseEvent event) {
        String user = null;
        String pswd = null;

        if (event.getClickCount() == 1) {
            try {
                stmt = con.prepareStatement("select * from login");
                ResultSet res = stmt.executeQuery();
                if (res.next()) {
                    user = res.getString("username");
                    pswd = res.getString("password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (txtUsername.getText().equals(user) && txtPassword.getText().equals(pswd)) {
                enableAllButtons();
                incorrectAttempts = 0; // Reset incorrect attempts counter
            } else {
                incorrectAttempts++;
                if (incorrectAttempts >= 3) {
                    disableTextFieldsForDelay();
                } else {
                    disableAllButtons();
                    showMymsg("Invalid Username or Password");
                }
            }
        }
    }

    private void enableAllButtons() {
        idOpenCustomerEnrollment.setDisable(false);
        idOpenMeasurements.setDisable(false);
        idOpenWorkerConsole.setDisable(false);
        idOpenWorkerTable.setDisable(false);
        idOpenItemRecords.setDisable(false);
        idOpenOrderDeliveryPanel.setDisable(false);
        idOpenGetReadyProducts.setDisable(false);
    }

    private void disableAllButtons() {
        idOpenCustomerEnrollment.setDisable(true);
        idOpenMeasurements.setDisable(true);
        idOpenWorkerConsole.setDisable(true);
        idOpenWorkerTable.setDisable(true);
        idOpenItemRecords.setDisable(true);
        idOpenOrderDeliveryPanel.setDisable(true);
        idOpenGetReadyProducts.setDisable(true);
    }


    private void disableTextFieldsForDelay() {
        txtUsername.setDisable(true);
        txtPassword.setDisable(true);

        // Schedule re-enabling after a delay
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            txtUsername.setDisable(false);
            txtPassword.setDisable(false);
        }));
        txtUsername.clear();
        txtPassword.clear();
        timeline.play();
    }

    @FXML
    void doDisable(ActionEvent event) {
        idOpenCustomerEnrollment.setDisable(true);
        idOpenMeasurements.setDisable(true);
        idOpenWorkerConsole.setDisable(true);
        idOpenWorkerTable.setDisable(true);
        idOpenItemRecords.setDisable(true);
        idOpenOrderDeliveryPanel.setDisable(true);
        idOpenGetReadyProducts.setDisable(true);

        Stage stage = (Stage) adminpanel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void doOpenCustomerEnrollment(ActionEvent event) throws IOException {
        openNewWindow("customerenrollmentt/CustomerEnrollmentView.fxml");
    }

    @FXML
    void doOpenGetReadyProducts(ActionEvent event) throws IOException {
        openNewWindow("readyproductss/ReadyProductsView.fxml");
    }

    @FXML
    void doOpenItemRecords(ActionEvent event) throws IOException {
        openNewWindow("measurementsexplorerr/itemRecordView.fxml");
    }

    @FXML
    void doOpenMeasurements(ActionEvent event) throws IOException {
        openNewWindow("measurementss/MeasurementsView.fxml");
    }

    @FXML
    void doOpenOrderDeliveryPanel(ActionEvent event) throws IOException {
        openNewWindow("orderdeliverypanell/OrderDeliveryPanelView.fxml");
    }

    @FXML
    void doOpenWorkerConsole(ActionEvent event) throws IOException {
        openNewWindow("workerconsolee/WorkerConsoleView.fxml");
    }

    @FXML
    void doOpenWorkerTable(ActionEvent event) throws IOException {
        openNewWindow("workertablevieww/WorkerTableView.fxml");
    }

    private void openNewWindow(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }
    int workers(String query) throws SQLException {
        ResultSet res = stmt.executeQuery(query);
        int count=0;
        while (res.next())
            count++;
        return count;
    }

    Connection con=null;
    PreparedStatement stmt;

    void fetchdata() throws SQLException {
        int count=0;

        try{
            stmt = con.prepareStatement("select count(*) as total from measurements where status = 1");
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                count = res.getInt("total");
                txtProcessOrders.setText(String.valueOf(count));
                txtpendingorders.setText(String.valueOf(count));
            }
        }
        catch (Exception exp){
            exp.printStackTrace();
        }


        int totalWorkers = workers("select * from workers");
        int occupiedWorkers = workers("select distinct wname from measurements");

        txtWorkerAvail.setText(String.valueOf(totalWorkers-occupiedWorkers));
        txtTotalOrdersDone.setText(String.valueOf(totalWorkers));

    }

    private void refresh() {
        // Create a Timeline to refresh data every 5 seconds
        Timeline refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            try {
                fetchdata();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQLException appropriately in a real application
            }
        }));
        refreshTimeline.setCycleCount(Animation.INDEFINITE); // Run indefinitely
        refreshTimeline.play();
    }


    @FXML
    void initialize() throws SQLException {
        con = doConnect();
        if (con != null)
            System.out.println("Congo...");
        else
            System.out.println("Sorry...");

        fetchdata();
        refresh();
    }
}
