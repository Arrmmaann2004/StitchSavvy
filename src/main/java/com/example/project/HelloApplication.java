package com.example.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("measurementss/MeasurementsView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("workerconsolee/WorkerConsoleView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("customerenrollmentt/CustomerEnrollmentView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("readyproductss/ReadyProductsView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("workertablevieww/WorkerTableView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("measurementsexplorerr/itemRecordView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("orderdeliverypanell/OrderDeliveryPanelView.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admindashboardd/AdminDashBoardView.fxml"));
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root);
//        Scene scene = new Scene(fxmlLoader.load(), 929, 674);
        stage.setTitle("Admin Panel");
        stage.setScene(scene);
//        stage.setFullScreen(true);
//        stage.initStyle(StageStyle.UNDECORATED);
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}