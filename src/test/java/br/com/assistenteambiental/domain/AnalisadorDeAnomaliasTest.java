package br.com.assistenteambiental.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalisadorDeAnomaliasTest {

    @Test
    void deveDetectarTemperaturaAlta() {

        LeituraTelemetria leitura = new LeituraTelemetria(
                "TEMPERATURA",
                35.0,
                "°C",
                LocalDateTime.now()
        );

        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

        boolean resultado = analisador.temAnomalia(leitura);

        assertTrue(resultado);
    }

    @Test
    void naoDeveDetectarAnomaliaQuandoTemperaturaEstiverNormal() {

        LeituraTelemetria leitura = new LeituraTelemetria(
                "TEMPERATURA",
                25.0,
                "°C",
                LocalDateTime.now()
        );

        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

        boolean resultado = analisador.temAnomalia(leitura);

        assertFalse(resultado);
    }

    @Test
    void deveDetectarUmidadeBaixa() {

        LeituraTelemetria leitura = new LeituraTelemetria(
                "UMIDADE",
                25.0,
                "%",
                LocalDateTime.now()
        );

        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

        boolean resultado = analisador.temAnomalia(leitura);

        assertTrue(resultado);
    }

    @Test
    void naoDeveDetectarAnomaliaQuandoUmidadeEstiverNormal() {

        LeituraTelemetria leitura = new LeituraTelemetria(
                "UMIDADE",
                50.0,
                "%",
                LocalDateTime.now()
        );

        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

        boolean resultado = analisador.temAnomalia(leitura);

        assertFalse(resultado);
    }

    @Test
    void deveDetectarLuminosidadeBaixa() {

        LeituraTelemetria leitura = new LeituraTelemetria(
                "LUMINOSIDADE",
                50.0,
                "lux",
                LocalDateTime.now()
        );

        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

        boolean resultado = analisador.temAnomalia(leitura);

        assertTrue(resultado);
    }

    @Test
    void deveGerarAlertaQuandoTemperaturaEstiverAlta() {

        LeituraTelemetria leitura = new LeituraTelemetria(
                "TEMPERATURA",
                35.0,
                "°C",
                LocalDateTime.now()
        );

        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

        Alerta alerta = analisador.analisar(leitura);

        assertNotNull(alerta);
        assertEquals("TEMPERATURA", alerta.getTipoSensor());
        assertEquals(35.0, alerta.getValor());
        assertEquals(
                "Temperatura acima do limite",
                alerta.getMensagem()
        );
    }

    @Test
    void naoDeveGerarAlertaQuandoTemperaturaEstiverNormal() {

        LeituraTelemetria leitura = new LeituraTelemetria(
                "TEMPERATURA",
                25.0,
                "°C",
                LocalDateTime.now()
        );

        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();

        Alerta alerta = analisador.analisar(leitura);

        assertNull(alerta);
    }
}