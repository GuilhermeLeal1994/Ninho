package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.LeituraTelemetria;
import br.com.assistenteambiental.domain.Sensor;
import br.com.assistenteambiental.domain.TipoSensor;
import br.com.assistenteambiental.infra.FilaDeLeituras;
import org.junit.jupiter.api.RepeatedTest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class ConcorrenciaSensoresTest {
    @RepeatedTest(10)
    void tresProdutoresUmConsumidorSemPerdaDeLeituras() throws InterruptedException {
        int porSensor = 100;
        int total = porSensor * 3;
        FilaDeLeituras fila = new FilaDeLeituras();
        ProcessadorDeLeituras consumidor = new ProcessadorDeLeituras(
                fila.getFila(), total, new AnalisadorDeAnomalias());
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(consumidor);
        for (TipoSensor tipo : TipoSensor.values()) {
            executor.submit(new Sensor(tipo, fila.getFila(), porSensor));
        }
        executor.shutdown();
        boolean terminou = executor.awaitTermination(10, TimeUnit.SECONDS);
        if (!terminou) executor.shutdownNow();
        assertTrue(terminou, "Threads devem terminar sem bloqueio");
        List<LeituraTelemetria> processadas = consumidor.getProcessadas();
        assertEquals(total, processadas.size());
        assertEquals(0, fila.getFila().size());
        for (TipoSensor tipo : TipoSensor.values()) {
            assertEquals(porSensor, processadas.stream()
                    .filter(l -> l.getTipoSensorEnum() == tipo).count());
        }
    }
}
