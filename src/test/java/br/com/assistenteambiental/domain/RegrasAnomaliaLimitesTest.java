package br.com.assistenteambiental.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class RegrasAnomaliaLimitesTest {
    private final AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

    private LeituraTelemetria leitura(TipoSensor tipo, double valor) {
        return new LeituraTelemetria(tipo, valor, "unidade", LocalDateTime.now());
    }

    @Test void temperaturaNoLimiteNaoAlerta() {
        assertFalse(analisador.temAnomalia(leitura(TipoSensor.TEMPERATURA, 30.0)));
    }
    @Test void temperaturaAcimaDoLimiteAlerta() {
        assertTrue(analisador.temAnomalia(leitura(TipoSensor.TEMPERATURA, 30.1)));
    }
    @Test void umidadeNoLimiteNaoAlerta() {
        assertFalse(analisador.temAnomalia(leitura(TipoSensor.UMIDADE, 30.0)));
    }
    @Test void umidadeAbaixoDoLimiteAlerta() {
        assertTrue(analisador.temAnomalia(leitura(TipoSensor.UMIDADE, 29.9)));
    }
    @Test void luminosidadeNoLimiteNaoAlerta() {
        assertFalse(analisador.temAnomalia(leitura(TipoSensor.LUMINOSIDADE, 100.0)));
    }
    @Test void luminosidadeAbaixoDoLimiteAlerta() {
        assertTrue(analisador.temAnomalia(leitura(TipoSensor.LUMINOSIDADE, 99.9)));
    }
    @Test void construtorEnumMantemTipo() {
        assertEquals(TipoSensor.TEMPERATURA, leitura(TipoSensor.TEMPERATURA, 25).getTipoSensorEnum());
    }
    @Test void valorNaoFinitoEhRejeitado() {
        assertThrows(IllegalArgumentException.class,
                () -> leitura(TipoSensor.TEMPERATURA, Double.NaN));
    }
}
