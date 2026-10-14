package br.com.assistenteambiental.app;

import org.junit.jupiter.api.RepeatedTest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class SincronizacaoTest {
    private static class ContadorSeguro {
        private int valor;
        synchronized void incrementar() { valor++; }
        synchronized int valor() { return valor; }
    }

    @RepeatedTest(10)
    void incrementoSincronizadoNaoPerdeAtualizacoes() throws Exception {
        int trabalhadores = 8;
        int incrementos = 1000;
        ContadorSeguro contador = new ContadorSeguro();
        ExecutorService executor = Executors.newFixedThreadPool(trabalhadores);
        List<Future<?>> tarefas = new ArrayList<>();
        try {
            for (int i = 0; i < trabalhadores; i++) {
                tarefas.add(executor.submit(() -> {
                    for (int j = 0; j < incrementos; j++) contador.incrementar();
                }));
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
            for (Future<?> tarefa : tarefas) tarefa.get(); // propaga erros das threads
            assertEquals(trabalhadores * incrementos, contador.valor());
        } finally {
            executor.shutdownNow();
        }
    }
}
