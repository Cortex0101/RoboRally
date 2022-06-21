module com.roborally {
  requires javafx.controls;
  requires javafx.base;
  requires javafx.graphics;

  requires org.jetbrains.annotations;
  requires com.google.gson;

  opens com.roborally to javafx.fxml;
  opens com.roborally.controller to com.google.gson;
  exports com.roborally;
  exports com.roborally.fileaccess.model to com.google.gson;
  exports com.roborally.controller;
  exports com.roborally.model;
}