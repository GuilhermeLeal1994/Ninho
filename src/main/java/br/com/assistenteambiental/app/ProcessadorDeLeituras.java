package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.Alerta;
import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.LeituraTelemetria;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Consome a quantidade esperada de leituras, sem polling e sem espera artificial. */
public class ProcessadorDeLeituras implements Runnable {
    private final BlockingQueue<LeituraTelemetria> fila;
    private final int quantidadeEsperada;
    private final CountDownLatch produtoresRestantes;
    private final AnalisadorDeAnomalias analisador;
    private final List<LeituraTelemetria> processadas = new ArrayList<>();
    private final List<Alerta> alertas = new ArrayList<>();

    public ProcessadorDeLeituras(BlockingQueue<LeituraTelemetria> fila, int quantidadeEsperada,
                                AnalisadorDeAnomalias analisador) {
        this(fila, quantidadeEsperada, analisador, null);
    }

    public ProcessadorDeLeituras(BlockingQueue<LeituraTelemetria> fila, int quantidadeEsperada,
                                AnalisadorDeAnomalias analisador, CountDownLatch produtoresRestantes) {
        this.fila = Objects.requireNonNull(fila, "fila");
        this.analisador = Objects.requireNonNull(analisador, "analisador");
        if (quantidadeEsperada < 0) throw new IllegalArgumentException("quantidade negativa");
        this.quantidadeEsperada = quantidadeEsperada;
        this.produtoresRestantes = produtoresRestantes;
    }

    @Override
    public void run() {
        try {
            if (produtoresRestantes == null) {
                for (int i = 0; i < quantidadeEsperada; i++) {
                    processar(fila.take());
                }
            } else {
                // Encerramento cooperativo: só termina quando todos os produtores
                // terminaram e não há leituras pendentes.
                while (produtoresRestantes.getCount() > 0 || !fila.isEmpty()) {
                    LeituraTelemetria leitura = fila.poll(100, TimeUnit.MILLISECONDS);
                    if (leitura != null) processar(leitura);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processar(LeituraTelemetria leitura) {
        processadas.add(leitura);
        Alerta alerta = analisador.analisar(leitura);
        if (alerta != null) alertas.add(alerta);
    }

    /** Consulte estes resultados somente após a thread consumidora terminar. */
    public List<LeituraTelemetria> getProcessadas() { return List.copyOf(processadas); }
    public List<Alerta> getAlertas() { return List.copyOf(alertas); }
}
