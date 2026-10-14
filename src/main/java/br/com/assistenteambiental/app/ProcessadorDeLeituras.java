package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.Alerta;
import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.LeituraTelemetria;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/** Consome a quantidade esperada de leituras, sem polling e sem espera artificial. */
public class ProcessadorDeLeituras implements Runnable {
    private final BlockingQueue<LeituraTelemetria> fila;
    private final int quantidadeEsperada;
    private final AnalisadorDeAnomalias analisador;
    private final List<LeituraTelemetria> processadas = new ArrayList<>();
    private final List<Alerta> alertas = new ArrayList<>();

    public ProcessadorDeLeituras(BlockingQueue<LeituraTelemetria> fila, int quantidadeEsperada,
                                AnalisadorDeAnomalias analisador) {
        this.fila = Objects.requireNonNull(fila, "fila");
        this.analisador = Objects.requireNonNull(analisador, "analisador");
        if (quantidadeEsperada < 0) throw new IllegalArgumentException("quantidade negativa");
        this.quantidadeEsperada = quantidadeEsperada;
    }

    @Override
    public void run() {
        for (int i = 0; i < quantidadeEsperada; i++) {
            try {
                LeituraTelemetria leitura = fila.take();
                processadas.add(leitura);
                Alerta alerta = analisador.analisar(leitura);
                if (alerta != null) alertas.add(alerta);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /** Consulte estes resultados somente após a thread consumidora terminar. */
    public List<LeituraTelemetria> getProcessadas() { return List.copyOf(processadas); }
    public List<Alerta> getAlertas() { return List.copyOf(alertas); }
}
