package com.ritallus.desvare.ui.viewmodel.gpg.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GpgKeyDto {
    private String fingerprint;
    private String publicKey;       // Llave original completa (para copiar)
    private String publicKeyBase64; // Primeros 20 caracteres del Base64 (para mostrar)
    private String name;
    private String email;
    private String createdAt;
}
