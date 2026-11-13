package com.example.chatserver;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ChatMetrics {

    private final MeterRegistry registry;
    private final AtomicInteger conectados;

    public ChatMetrics(MeterRegistry registry) {
        this.registry = registry;
        this.conectados = registry.gauge("chat.usuarios.conectados", new AtomicInteger(0));
    }

    public void mensaje(String tipo) {
        registry.counter("chat.mensajes", "tipo", tipo).increment();
    }

    public void entra() {
        if (conectados != null) {
            conectados.incrementAndGet();
        }
    }

    public void sale() {
        if (conectados != null && conectados.get() > 0) {
            conectados.decrementAndGet();
        }
    }
}
