module com.example.demod {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires javafx.media;


    opens com.example.demod to javafx.fxml;
    exports com.example.demod;
}