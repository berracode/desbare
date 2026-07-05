package com.ritallus.desvare.ui.controller.custom.button;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;

/*
No necesita FXML porque hereda de un control unico,
necesitaria de FXML cuando combino HBox con button y text field por ejemplo,
o cuando heredo de HBox, Vhox, AnchorPane, etc (layouts)
 */
public class LoadingButton extends Button {

    // Propiedad reactiva para controlar el estado de carga desde afuera
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    // Propiedad para personalizar el texto de carga desde el FXML o Controller
    private final StringProperty loadingText = new SimpleStringProperty("Procesando...");

    private final ProgressIndicator spinner = new ProgressIndicator();
    private String originalText = "";

    public LoadingButton() {
        super();
        initComponent();
    }

    private void initComponent() {
        this.getStyleClass().add("loading-button");

        spinner.setMaxSize(18, 18);
        spinner.setProgress(-1); // Animación indeterminada

        // Escuchar los cambios en la propiedad 'loading'
        loading.addListener((obs, wasLoading, isLoading) -> {
            if (isLoading) {
                originalText = (getText() != null) ? getText() : "";

                //setDisable(true);
                this.getStyleClass().add("disabled");
                setMouseTransparent(true);
                setFocusTraversable(false); // Evita que se navegue con el teclado (Tab) mientras procesa

                setGraphic(spinner);
                setText(loadingText.get());
            } else {
                //setDisable(false);
                this.getStyleClass().remove("disabled");
                // Restaurar estado normal de interacción
                setMouseTransparent(false);
                setFocusTraversable(true);

                setGraphic(null);
                setText(originalText);
            }
        });
    }

    public BooleanProperty loadingProperty() {
        return loading;
    }

    public boolean isLoading() {
        return loading.get();
    }

    public void setLoading(boolean isLoading) {
        this.loading.set(isLoading);
    }

    public StringProperty loadingTextProperty() {
        return loadingText;
    }

    public String getLoadingText() {
        return loadingText.get();
    }

    public void setLoadingText(String text) {
        this.loadingText.set(text);
    }
}