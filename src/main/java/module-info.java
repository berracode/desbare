module co.com.bancolombia.desbare {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens co.com.bancolombia.desbare to javafx.fxml;
    exports co.com.bancolombia.desbare;


    // ESTA ES LA LÍNEA QUE DEBES AGREGAR:
    opens co.com.bancolombia.desbare.ui.controller to javafx.fxml;
}