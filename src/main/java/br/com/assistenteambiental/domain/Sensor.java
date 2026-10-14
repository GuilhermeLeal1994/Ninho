package br.com.assistenteambiental.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/** Produz leituras; não faz análise nem persistência. */
public class Sensor implements Runnable {
    private final TipoSensor tipo;
    private final BlockingQueue<LeituraTelemetria> fila;
    private final int quantidade;

    public Sensor(TipoSensor tipo, BlockingQueue<LeituraTelemetria> fila, int quantidade) {
        this.tipo = Objects.requireNonNull(tipo, "tipo");
        this.fila = Objects.requireNonNull(fila, "fila");
        if (quantidade < 0) throw new IllegalArgumentException("quantidade negativa");
        this.quantidade = quantidade;
    }

    @Override
    public void run() {
        for (int i = 0; i < quantidade; i++) {
            if (Thread.currentThread().isInterrupted()) return;
            try {
                fila.put(criarLeitura(i));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private LeituraTelemetria criarLeitura(int indice) {
        return switch (tipo) {
            case TEMPERATURA -> new LeituraTelemetria(tipo, 25.0 + indice % 8, "°C", LocalDateTime.now());
            case UMIDADE -> new LeituraTelemetria(tipo, 25.0 + indice % 30, "%", LocalDateTime.now());
            case LUMINOSIDADE -> new LeituraTelemetria(tipo, 80.0 + indice % 60, "lux", LocalDateTime.now());
        };
    }
}
