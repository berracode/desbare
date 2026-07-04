package com.ritallus.desvare.core.domain.ports.outbound;


public interface Base64ServicePort {
    String encode(String plainText);

    String decode(String base64Text);
}
