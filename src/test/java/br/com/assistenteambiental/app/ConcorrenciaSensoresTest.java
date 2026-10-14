package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.LeituraTelemetria;
import br.com.assistenteambiental.domain.Sensor;
import br.com.assistenteambiental.domain.TipoSensor;
import br.com.assistenteambiental.infra.FilaDeLeituras;
import org.junit.jupiter.api.RepeatedTest;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class ConcorrenciaSensoresTest {
    @RepeatedTest(10)
    void tresProdutoresUmConsumidorSemPerdaDeLeituras() throws Exception {
        int porSensor = 100;
        int total = porSensor * 3;
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
        boolean terminou = executor.awaitTermination(10, TimeUnit.SECONDS);
        if (!terminou) executor.shutdownNow();
        assertTrue(terminou, "Threads devem terminar sem bloqueio");
        for (Future<?> tarefa : tarefas) tarefa.get();
        List<LeituraTelemetria> processadas = consumidor.getProcessadas();
        assertEquals(total, processadas.size());
        assertEquals(0, fila.getFila().size());
        Set<String> identificadores = new HashSet<>();
        for (LeituraTelemetria leitura : processadas) {
            assertTrue(identificadores.add(leitura.getTipoSensor() + ":" + leitura.getSequencia()),
                    "Leitura duplicada: " + leitura.getTipoSensor() + ":" + leitura.getSequencia());
        }
        assertEquals(total, identificadores.size());
        for (TipoSensor tipo : TipoSensor.values()) {
            assertEquals(porSensor, processadas.stream()
                    .filter(l -> l.getTipoSensorEnum() == tipo).count());
        }
    }
}
