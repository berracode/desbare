module com.ritallus.desvare {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.slf4j;
    requires org.controlsfx.controls;
    requires static lombok;
    requires org.xerial.sqlitejdbc;
    requires org.bouncycastle.pg;
    requires org.bouncycastle.provider;
    requires javafx.graphics;

    opens com.ritallus.desvare to javafx.fxml;
    exports com.ritallus.desvare;

    //view models
    exports com.ritallus.desvare.ui.viewmodel.gpg.dto;
    exports com.ritallus.desvare.ui.controller.custom.button;

    // Controladores
    exports com.ritallus.desvare.ui.controller; // opcional si otros módulos lo usan
    opens com.ritallus.desvare.ui.controller to javafx.fxml;

    exports com.ritallus.desvare.ui.controller.sidebar; // opcional
    opens com.ritallus.desvare.ui.controller.sidebar to javafx.fxml;

    exports com.ritallus.desvare.ui.controller.tabs; // opcional
    opens com.ritallus.desvare.ui.controller.tabs to javafx.fxml;

    exports com.ritallus.desvare.ui.controller.commands.gpg;
    opens com.ritallus.desvare.ui.controller.commands.gpg to javafx.fxml;

    exports com.ritallus.desvare.ui.controller.commands.base64;
    opens com.ritallus.desvare.ui.controller.commands.base64 to javafx.fxml;


}