open module com.roborally {
  requires javafx.controls;
  requires javafx.base;
  requires javafx.graphics;

  requires org.jetbrains.annotations;
  requires com.google.gson;
  requires java.desktop;
  requires spring.boot.autoconfigure;
  requires spring.boot;
  requires spring.web;


  //opens com.roborally to javafx.fxml, spring.boot, spring.web, spring.boot.autoconfigure;
  //opens com.roborally.RESTapi to spring.web, spring.boot, spring.boot.autoconfigure;
  //opens com.roborally.controller to com.google.gson;
  exports com.roborally;
  exports com.roborally.fileaccess.model to com.google.gson;
  exports com.roborally.controller;
  exports com.roborally.model;
}