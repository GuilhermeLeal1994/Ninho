package br.com.assistenteambiental.app;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Exemplo didático isolado: não é usado na fila real do Ninho. */
public class DemonstracaoCondicaoCorrida {
    static class ContadorInseguro {
        private int valor;
        void incrementar() { valor++; } // leitura + incremento + escrita não são atômicos
        int getValor() { return valor; }
    }

    static class ContadorSeguro {
        private int valor;
        synchronized void incrementar() { valor++; }
        synchronized int getValor() { return valor; }
    }

    public static void main(String[] args) throws InterruptedException {
        final int trabalhadores = 8;
        final int incrementos = 100_000;
        final int esperado = trabalhadores * incrementos;
        for (int rodada = 1; rodada <= 3; rodada++) {
            ContadorInseguro inseguro = new ContadorInseguro();
            ContadorSeguro seguro = new ContadorSeguro();
            ExecutorService executor = Executors.newFixedThreadPool(trabalhadores);
            CountDownLatch inicio = new CountDownLatch(1);
            for (int i = 0; i < trabalhadores; i++) {
                executor.submit(() -> {
                    try {
                        inicio.await();
                        for (int j = 0; j < incrementos; j++) {
                            inseguro.incrementar();
                            seguro.incrementar();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            inicio.countDown();
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                throw new IllegalStateException("Execução não terminou");
            }
            System.out.printf("Rodada %d: esperado=%d, sem sincronização=%d, com synchronized=%d%n",
                    rodada, esperado, inseguro.getValor(), seguro.getValor());
        }
    }
}
