module com.mycompany.testingjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.testingjavafx to javafx.fxml;
    exports com.mycompany.testingjavafx;
}
