package br.com.assistenteambiental.infra;

import br.com.assistenteambiental.domain.TipoSensor;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class AppsScriptLeiturasTest {
    private static final String EXEMPLO = """
        {"ok":true,
         "temperature":[{"date":"2026-10-20","time":"21:27:08","value":23.6}],
         "humidity":[{"date":"2026-10-20","time":"21:27:08","value":55.5}],
         "luminosity":[{"date":"2026-10-20","time":"21:27:08","value":2055}]}
        """;

    @Test void interpretaTresSensoresEUnidades() throws Exception {
        var leituras = AppsScriptLeituras.interpretar(EXEMPLO);
        assertEquals(3, leituras.size());
        assertEquals(TipoSensor.LUMINOSIDADE, leituras.get(2).getTipoSensorEnum());
        assertEquals("ADC", leituras.get(2).getUnidade());
        assertEquals(2055, leituras.get(2).getValor());
    }

    @Test void aceitaHistoricoVazio() throws Exception {
        assertTrue(AppsScriptLeituras.interpretar(
            "{\"ok\":true,\"temperature\":[],\"humidity\":[],\"luminosity\":[]}").isEmpty());
    }

    @Test void rejeitaChaveInvalida() {
        assertThrows(IOException.class, () ->
            AppsScriptLeituras.interpretar("{\"ok\":false,\"error\":\"Chave inválida\"}"));
    }

    @Test void rejeitaCampoAusente() {
        assertThrows(IOException.class, () ->
            AppsScriptLeituras.interpretar("{\"ok\":true,\"temperature\":[],\"humidity\":[]}"));
    }

    @Test void rejeitaValorNaoNumerico() {
        assertThrows(IOException.class, () ->
            AppsScriptLeituras.interpretar(EXEMPLO.replace("2055", "\"erro\"")));
    }

    @Test void rejeitaDataInvalida() {
        assertThrows(IOException.class, () ->
            AppsScriptLeituras.interpretar(EXEMPLO.replace("2026-10-20", "20/10/2026")));
    }
}
