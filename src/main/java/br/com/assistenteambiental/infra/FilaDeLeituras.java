package br.com.assistenteambiental.infra;

import br.com.assistenteambiental.domain.LeituraTelemetria;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/** Centraliza a fila thread-safe compartilhada entre produtores e consumidor. */
public class FilaDeLeituras {
    private final BlockingQueue<LeituraTelemetria> leituras = new LinkedBlockingQueue<>();

    public void publicar(LeituraTelemetria leitura) throws InterruptedException {
        leituras.put(java.util.Objects.requireNonNull(leitura, "leitura"));
    }

    public LeituraTelemetria retirar() throws InterruptedException {
        return leituras.take();
    }

    /** Acesso legado mantido para compatibilidade com os produtores existentes. */
    public BlockingQueue<LeituraTelemetria> getFila() {
        return leituras;
    }
}
