package br.com.assistenteambiental.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import static org.junit.jupiter.api.Assertions.*;

class IntegracaoAdcTest {
    private final AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

    private LeituraTelemetria leitura(double valor, String unidade) {
        return new LeituraTelemetria(TipoSensor.LUMINOSIDADE, valor, unidade, LocalDateTime.now());
    }

    @Test void adcEscuroGeraAlerta() {
        assertTrue(analisador.temAnomalia(leitura(1200, "ADC")));
        assertNotNull(analisador.analisar(leitura(0, "ADC")));
    }

    @Test void adcIntermediarioEClaroNaoGeramAlerta() {
        assertFalse(analisador.temAnomalia(leitura(1201, "ADC")));
        assertNull(analisador.analisar(leitura(4095, "ADC")));
    }

    @Test void regraAntigaEmLuxPermaneceCompativel() {
        assertTrue(analisador.temAnomalia(leitura(99.9, "lux")));
        assertFalse(analisador.temAnomalia(leitura(100, "lux")));
    }

    @Test void rejeitaAdcFracionarioOuForaDaFaixa() {
        assertThrows(IllegalArgumentException.class, () -> analisador.temAnomalia(leitura(10.5, "ADC")));
        assertThrows(IllegalArgumentException.class, () -> analisador.temAnomalia(leitura(4096, "ADC")));
    }

    @Test void sensorAdcProduzLeiturasComUnidadeCorreta() {
        var fila = new LinkedBlockingQueue<LeituraTelemetria>();
        Sensor.luminosidadeAdc(fila, 3, null).run();
        assertEquals(3, fila.size());
        assertEquals("ADC", fila.peek().getUnidade());
        assertEquals(0.0, fila.poll().getValor());
        assertEquals(1500.0, fila.poll().getValor());
        assertEquals(3000.0, fila.poll().getValor());
    }
}
