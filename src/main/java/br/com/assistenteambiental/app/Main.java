package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.Sensor;
import br.com.assistenteambiental.domain.TipoSensor;
import br.com.assistenteambiental.infra.FilaDeLeituras;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final int porSensor = 10;
        final int total = porSensor * TipoSensor.values().length;
        FilaDeLeituras fila = new FilaDeLeituras();
        CountDownLatch produtoresRestantes = new CountDownLatch(TipoSensor.values().length);
        ProcessadorDeLeituras consumidor = new ProcessadorDeLeituras(
                fila.getFila(), total, new AnalisadorDeAnomalias(), produtoresRestantes);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<?>> tarefas = new ArrayList<>();
        tarefas.add(executor.submit(consumidor));
        for (TipoSensor tipo : TipoSensor.values()) {
            tarefas.add(executor.submit(new Sensor(tipo, fila.getFila(), porSensor, produtoresRestantes)));
        }
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            throw new IllegalStateException("Tempo limite excedido ao processar leituras");
        }
        for (Future<?> tarefa : tarefas) tarefa.get();
        if (consumidor.getProcessadas().size() != total) {
            throw new IllegalStateException("Quantidade de leituras divergente");
        }
        System.out.println("Leituras esperadas: " + total);
        System.out.println("Leituras processadas: " + consumidor.getProcessadas().size());
        System.out.println("Alertas gerados: " + consumidor.getAlertas().size());
    }
}
