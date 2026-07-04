package co.com.bancolombia.desbare.ui.viewmodel.base64;

import co.com.bancolombia.desbare.core.domain.ports.outbound.Base64ServicePort;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Base64ToolViewModel {

    private final Base64ServicePort base64Service; // Inyectado desde el dominio


    // --- PROPIEDADES REACTIVAS PARA ENCODE ---
    private final StringProperty encodeInput = new SimpleStringProperty("");
    private final StringProperty encodeOutput = new SimpleStringProperty("");

    // --- PROPIEDADES REACTIVAS PARA DECODE ---
    private final StringProperty decodeInput = new SimpleStringProperty("");
    private final StringProperty decodeOutput = new SimpleStringProperty("");

    // Getters de las propiedades para hacer el Binding en los Controladores: el bidireccional
    public StringProperty encodeInputProperty() {
        return encodeInput;
    }

    public StringProperty encodeOutputProperty() {
        return encodeOutput;
    }

    public StringProperty decodeInputProperty() {
        return decodeInput;
    }

    public StringProperty decodeOutputProperty() {
        return decodeOutput;
    }
    //esto puede estar en un servicio en la capa core

    /**
     * Lógica: Codificar texto plano a Base64
     */
    public void encode() {
        String inputText = encodeInput.get();
        if (inputText == null || inputText.isBlank()) {
            encodeOutput.set("");
            return;
        }
        try {
            log.info("Codificando bloque de texto a Base64. Tamaño: {}", inputText.length());
            String encoded = base64Service.encode(inputText);
            encodeOutput.set(encoded);
        } catch (Exception e) {
            log.error("Error inesperado al codificar Base64", e);
            encodeOutput.set("ERROR CRÍTICO: " + e.getMessage());
        }
    }

    /**
     * Lógica: Decodificar una cadena Base64 a texto plano
     */
    public void decode() {
        String inputText = decodeInput.get();
        if (inputText == null || inputText.isBlank()) {
            decodeOutput.set("");
            return;
        }
        try {
            log.info("Decodificando cadena Base64...");
            var decoded = base64Service.decode(inputText);
            decodeOutput.set(decoded);
        } catch (IllegalArgumentException e) {
            log.warn("La cadena proporcionada no es un Base64 válido: {}", e.getMessage());
            decodeOutput.set("ERROR: El texto ingresado no posee un formato Base64 válido.");
        } catch (Exception e) {
            log.error("Error inesperado al decodificar Base64", e);
            decodeOutput.set("ERROR CRÍTICO: " + e.getMessage());
        }
    }
}
