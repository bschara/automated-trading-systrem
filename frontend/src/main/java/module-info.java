module com.example.trading {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires okhttp3;
    requires org.apache.commons.codec;
    requires com.google.gson;
    requires java.xml.crypto;
    requires org.json;
    requires org.slf4j;
    requires java.desktop;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens com.example.trading to javafx.fxml;
    opens com.example.trading.controllers to javafx.fxml, javafx.base;
    opens com.example.trading.util to javafx.base;


    exports com.example.trading;
    exports com.example.trading.controllers to javafx.fxml;
}