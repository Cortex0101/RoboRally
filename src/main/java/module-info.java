module com.roborally {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires annotations;
    requires com.google.gson;
    requires com.google.common;

    opens com.roborally to javafx.fxml;
    exports com.roborally;
    exports com.roborally.fileaccess.model to com.google.gson;
    exports com.roborally.model to com.google.gson;
}