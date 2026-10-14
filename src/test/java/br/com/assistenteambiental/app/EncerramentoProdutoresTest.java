package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.Sensor;
import br.com.assistenteambiental.domain.TipoSensor;
import br.com.assistenteambiental.infra.FilaDeLeituras;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class EncerramentoProdutoresTest {
    @Test
    void consumidorTerminaMesmoQuandoProducaoEhMenorQuePrevisto() throws Exception {
        FilaDeLeituras fila = new FilaDeLeituras();
        CountDownLatch fim = new CountDownLatch(1);
        ProcessadorDeLeituras consumidor =
                new ProcessadorDeLeituras(fila.getFila(), 100, new AnalisadorDeAnomalias(), fim);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<?> consumo = executor.submit(consumidor);
            Future<?> producao = executor.submit(new Sensor(TipoSensor.TEMPERATURA, fila.getFila(), 5, fim));
            producao.get(5, TimeUnit.SECONDS);
            consumo.get(5, TimeUnit.SECONDS);
            assertEquals(5, consumidor.getProcessadas().size());
            assertTrue(fila.getFila().isEmpty());
        } finally {
            executor.shutdownNow();
        }
    }
}
