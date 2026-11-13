package com.example.chatserver;

import java.time.LocalTime;

public record ChatMessage(String tipo, String autor, String contenido, String fecha) {

    public ChatMessage conFecha() {
        return new ChatMessage(tipo, autor, contenido, LocalTime.now().withNano(0).toString());
    }
}
