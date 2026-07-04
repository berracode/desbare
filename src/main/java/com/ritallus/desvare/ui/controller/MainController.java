package com.ritallus.desvare.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

public class MainController {


    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private SplitPane debugSplitPane;


    @FXML
    public void initialize() {
        mainSplitPane.setDividerPosition(0, 250.0 / mainSplitPane.getWidth());

        mainSplitPane.widthProperty().addListener((obs, oldW, newW) -> {
            mainSplitPane.setDividerPosition(0, 250.0 / newW.doubleValue());
        });

        debugSplitPane.setDividerPositions(0.9); // más alto para el contenido

        debugSplitPane.setDividerPosition(0, 1.0 - (250.0 / debugSplitPane.getHeight()));
        debugSplitPane.heightProperty().addListener((obs, oldH, newH) -> {
            debugSplitPane.setDividerPosition(0, 1.0 - (250.0 / newH.doubleValue()));
        });
    }
}
