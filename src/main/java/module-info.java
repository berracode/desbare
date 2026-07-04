module co.com.bancolombia.desbare {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.slf4j;
    requires org.controlsfx.controls;
    requires static lombok;
    requires org.xerial.sqlitejdbc;
    requires org.bouncycastle.pg;
    requires org.bouncycastle.provider;

    opens co.com.bancolombia.desbare to javafx.fxml;
    exports co.com.bancolombia.desbare;

    //view models
    exports co.com.bancolombia.desbare.ui.viewmodel.gpg.dto;

    // Controladores
    exports co.com.bancolombia.desbare.ui.controller; // opcional si otros módulos lo usan
    opens co.com.bancolombia.desbare.ui.controller to javafx.fxml;

    exports co.com.bancolombia.desbare.ui.controller.sidebar; // opcional
    opens co.com.bancolombia.desbare.ui.controller.sidebar to javafx.fxml;

    exports co.com.bancolombia.desbare.ui.controller.tabs; // opcional
    opens co.com.bancolombia.desbare.ui.controller.tabs to javafx.fxml;

    exports co.com.bancolombia.desbare.ui.controller.commands.gpg;
    opens co.com.bancolombia.desbare.ui.controller.commands.gpg to javafx.fxml;

    exports co.com.bancolombia.desbare.ui.controller.commands.base64;
    opens co.com.bancolombia.desbare.ui.controller.commands.base64 to javafx.fxml;


}