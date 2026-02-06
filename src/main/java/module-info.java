module com.svalero.fitnesscenter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.svalero.fitnesscenter to javafx.fxml;
    exports com.svalero.fitnesscenter;
}