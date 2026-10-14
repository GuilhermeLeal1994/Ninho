package br.com.assistenteambiental.infra;

import br.com.assistenteambiental.domain.LeituraTelemetria;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/** Centraliza a fila thread-safe compartilhada entre produtores e consumidor. */
public class FilaDeLeituras {
    private final BlockingQueue<LeituraTelemetria> leituras = new LinkedBlockingQueue<>();

    public BlockingQueue<LeituraTelemetria> getFila() {
        return leituras;
    }
}
