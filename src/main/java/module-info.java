module com.example.dictionary {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires javafx.web;
    requires org.json;
    requires freetts;
    requires jlayer;

    opens com.example.dictionary to javafx.fxml;
    exports com.example.dictionary;
}