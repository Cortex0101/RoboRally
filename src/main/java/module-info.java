module com.roborally {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;

    requires annotations;
    requires com.google.gson;
    requires com.google.common;

    opens com.roborally to javafx.fxml;
    opens com.roborally.controller to com.google.gson;
    exports com.roborally;
    exports com.roborally.fileaccess.model to com.google.gson;
    exports com.roborally.model to com.google.gson;
}