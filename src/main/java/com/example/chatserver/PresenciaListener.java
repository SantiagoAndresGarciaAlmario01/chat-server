package com.example.chatserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class PresenciaListener {

    private static final Logger log = LoggerFactory.getLogger(PresenciaListener.class);
    private final SimpMessagingTemplate broker;

    public PresenciaListener(SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    @EventListener
    public void alDesconectar(SessionDisconnectEvent evento) {
        Map<String, Object> sesion = StompHeaderAccessor.wrap(evento.getMessage()).getSessionAttributes();
        String usuario = sesion == null ? null : (String) sesion.get("usuario");
        if (usuario == null) return;
        log.info("Salió {}", usuario);
        broker.convertAndSend("/topic/sala",
                new ChatMessage("SALIDA", usuario, usuario + " salió del chat", null).conFecha());
    }
}
