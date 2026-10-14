package br.com.assistenteambiental.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/** Produz leituras; não faz análise nem persistência. */
public class Sensor implements Runnable {
    private final TipoSensor tipo;
    private final BlockingQueue<LeituraTelemetria> fila;
    private final int quantidade;
    private final CountDownLatch produtoresRestantes;

    public Sensor(TipoSensor tipo, BlockingQueue<LeituraTelemetria> fila, int quantidade) {
        this(tipo, fila, quantidade, null);
    }

    public Sensor(TipoSensor tipo, BlockingQueue<LeituraTelemetria> fila, int quantidade,
                  CountDownLatch produtoresRestantes) {
        this.tipo = Objects.requireNonNull(tipo, "tipo");
        this.fila = Objects.requireNonNull(fila, "fila");
        if (quantidade < 0) throw new IllegalArgumentException("quantidade negativa");
        this.quantidade = quantidade;
        this.produtoresRestantes = produtoresRestantes;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < quantidade; i++) {
                if (Thread.currentThread().isInterrupted()) return;
                try {
                    fila.put(criarLeitura(i));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } finally {
            if (produtoresRestantes != null) produtoresRestantes.countDown();
        }
    }

    private LeituraTelemetria criarLeitura(int indice) {
        return switch (tipo) {
            case TEMPERATURA -> new LeituraTelemetria(tipo, 25.0 + indice % 8, "°C", LocalDateTime.now(), indice);
            case UMIDADE -> new LeituraTelemetria(tipo, 25.0 + indice % 30, "%", LocalDateTime.now(), indice);
            case LUMINOSIDADE -> new LeituraTelemetria(tipo, 80.0 + indice % 60, "lux", LocalDateTime.now(), indice);
        };
    }
}
