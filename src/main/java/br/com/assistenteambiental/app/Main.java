package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.Sensor;
import br.com.assistenteambiental.domain.TipoSensor;
import br.com.assistenteambiental.infra.FilaDeLeituras;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int porSensor = 10;
        final int total = porSensor * TipoSensor.values().length;
        FilaDeLeituras fila = new FilaDeLeituras();
        ProcessadorDeLeituras consumidor = new ProcessadorDeLeituras(
                fila.getFila(), total, new AnalisadorDeAnomalias());

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(consumidor);
        for (TipoSensor tipo : TipoSensor.values()) {
            executor.submit(new Sensor(tipo, fila.getFila(), porSensor));
        }
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            throw new IllegalStateException("Tempo limite excedido ao processar leituras");
        }
        System.out.println("Leituras esperadas: " + total);
        System.out.println("Leituras processadas: " + consumidor.getProcessadas().size());
        System.out.println("Alertas gerados: " + consumidor.getAlertas().size());
    }
}
