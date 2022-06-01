module com.mycompany.texteditor {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.texteditor to javafx.fxml;
    exports com.mycompany.texteditor;
}
