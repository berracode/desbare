package com.ritallus.desvare.core.domain.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.ritallus.desvare.core.domain.ports.outbound.Base64ServicePort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Base64Service implements Base64ServicePort {

    @Override
    public String encode(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            throw new IllegalArgumentException("La cadena proporcionada es null o vacio");
        }
        try {
            log.info("Codificando bloque de texto a Base64. Tamaño: {}", plainText.length());
            return Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Error inesperado al codificar Base64", e);
            throw e;
        }
    }

    @Override
    public String decode(String base64Text) {
        if (base64Text == null || base64Text.isBlank()) {
            throw new IllegalArgumentException("La cadena proporcionada no es un Base64 válido: " + base64Text);
        }
        try {
            log.info("Decodificando cadena Base64...");
            // Limpiamos posibles espacios en blanco o saltos de línea comunes al pegar datos
            String cleanInput = base64Text.trim().replaceAll("\\s", "");
            byte[] decodedBytes = Base64.getDecoder().decode(cleanInput);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            log.warn("La cadena proporcionada no es un Base64 válido: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al decodificar Base64", e);
            throw new RuntimeException("Error inesperado al decodificar Base64", e);
        }
    }
}
