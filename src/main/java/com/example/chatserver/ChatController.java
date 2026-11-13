package com.example.chatserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatMetrics metrics;

    public ChatController(ChatMetrics metrics) {
        this.metrics = metrics;
    }

    @MessageMapping("/chat.unirse")
    @SendTo("/topic/sala")
    public ChatMessage unirse(ChatMessage mensaje, SimpMessageHeaderAccessor headers) {
        if (headers.getSessionAttributes() != null) {
            headers.getSessionAttributes().put("usuario", mensaje.autor());
        }
        metrics.entra();
        log.info("Se unió {}", mensaje.autor());
        return new ChatMessage("UNION", mensaje.autor(), mensaje.autor() + " entró al chat", null).conFecha();
    }

    @MessageMapping("/chat.enviar")
    @SendTo("/topic/sala")
    public ChatMessage enviar(ChatMessage mensaje) {
        metrics.mensaje(mensaje.tipo());
        log.info("Mensaje {} de {}", mensaje.tipo(), mensaje.autor());
        return mensaje.conFecha();
    }

    @MessageMapping("/chat.escribiendo")
    @SendTo("/topic/escribiendo")
    public String escribiendo(String usuario) {
        return usuario;
    }
}
