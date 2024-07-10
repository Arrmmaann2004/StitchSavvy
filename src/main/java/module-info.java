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
}