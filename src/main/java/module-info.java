module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.project to javafx.fxml;
    exports com.example.project;

    exports com.example.project.customerenrollment;
    opens com.example.project.customerenrollment to javafx.fxml;

    exports com.example.project.workerconsole;
    opens com.example.project.workerconsole to javafx.fxml;

    exports com.example.project.measurements;
    opens com.example.project.measurements to javafx.fxml;

    exports com.example.project.readyproducts;
    opens com.example.project.readyproducts to javafx.fxml;

    exports com.example.project.workertableview;
    opens com.example.project.workertableview to javafx.fxml;

    exports com.example.project.measurementsexplorer;
    opens com.example.project.measurementsexplorer to javafx.fxml;

    exports com.example.project.orderdeliverypanel;
    opens com.example.project.orderdeliverypanel to javafx.fxml;

    exports com.example.project.admindashboard;
    opens com.example.project.admindashboard to javafx.fxml;
}