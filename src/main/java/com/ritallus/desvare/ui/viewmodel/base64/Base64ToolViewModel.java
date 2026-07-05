package com.ritallus.desvare.ui.viewmodel.base64;

import com.ritallus.desvare.core.domain.ports.outbound.Base64ServicePort;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Base64ToolViewModel {

    private final Base64ServicePort base64Service;

    // --- PROPIEDADES REACTIVAS PARA ENCODE ---
    private final StringProperty encodeInput = new SimpleStringProperty("");
    private final StringProperty encodeOutput = new SimpleStringProperty("");
    private final BooleanProperty isEncoding = new SimpleBooleanProperty(false); // NUEVO

    // --- PROPIEDADES REACTIVAS PARA DECODE ---
    private final StringProperty decodeInput = new SimpleStringProperty("");
    private final StringProperty decodeOutput = new SimpleStringProperty("");
    private final BooleanProperty isDecoding = new SimpleBooleanProperty(false); // NUEVO

    // --- PROPIEDAD DE ERROR GLOBAL/LOCAL (UX sugerido antes) ---
    private final StringProperty errorMessage = new SimpleStringProperty(""); // NUEVO

    // --- GETTERS PARA LOS BINDINGS ---
    public StringProperty encodeInputProperty() {
        return encodeInput;
    }

    public StringProperty encodeOutputProperty() {
        return encodeOutput;
    }

    public BooleanProperty isEncodingProperty() {
        return isEncoding;
    } // NUEVO

    public StringProperty decodeInputProperty() {
        return decodeInput;
    }

    public StringProperty decodeOutputProperty() {
        return decodeOutput;
    }

    public BooleanProperty isDecodingProperty() {
        return isDecoding;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void encode() {
        String inputText = encodeInput.get();
        if (inputText == null || inputText.isBlank()) {
            encodeOutput.set("");
            errorMessage.set("");
            return;
        }

        // 1. Encendemos el estado de carga y limpiamos errores anteriores
        isEncoding.set(true);
        errorMessage.set("");

        Thread.startVirtualThread(() -> {
            try {
                log.info("Codificando bloque de texto a Base64. Tamaño: {}", inputText.length());
                String encoded = base64Service.encode(inputText);
                Platform.runLater(() -> encodeOutput.set(encoded)); //PINTAR EN ui USANDO EL hILO DE JAVA FX

            } catch (Exception e) {
                log.error("Error inesperado al codificar Base64", e);
                Platform.runLater(() -> errorMessage.set("ERROR CRÍTICO: " + e.getMessage()));
            } finally {
                Platform.runLater(() -> isEncoding.set(false));
            }
        });
    }

    public void decode() {
        String inputText = decodeInput.get();
        if (inputText == null || inputText.isBlank()) {
            decodeOutput.set("");
            errorMessage.set("No estás enviando nada!!");
            return;
        }

        isDecoding.set(true);
        errorMessage.set("");

        Thread.startVirtualThread(() -> {
            try {
                log.info("Decodificando cadena Base64...");
                String decoded = base64Service.decode(inputText);

                Platform.runLater(() -> {
                    decodeOutput.set(decoded);
                    errorMessage.set("");
                });

            } catch (IllegalArgumentException e) {
                log.warn("La cadena proporcionada no es un Base64 válido: {}", e.getMessage());
                Platform.runLater(() -> {
                    errorMessage.set("ERROR: El texto ingresado no posee un formato Base64 válido.");
                    decodeOutput.set("");
                });
            } catch (Exception e) {
                log.error("Error inesperado al decodificar Base64", e);
                Platform.runLater(() -> {
                    errorMessage.set("ERROR CRÍTICO: " + e.getMessage());
                    decodeOutput.set("");
                });
            } finally {
                Platform.runLater(() -> isDecoding.set(false));
            }
        });
    }
}