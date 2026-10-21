package br.com.assistenteambiental.infra;

import br.com.assistenteambiental.domain.LeituraTelemetria;
import br.com.assistenteambiental.domain.TipoSensor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Traduz o JSON do Apps Script em leituras do dominio. */
public final class AppsScriptLeituras {
    private static final ObjectMapper JSON = new ObjectMapper();

    private AppsScriptLeituras() {}

    public static List<LeituraTelemetria> interpretar(String corpo) throws IOException {
        JsonNode raiz = JSON.readTree(corpo);
        if (raiz == null || !raiz.isObject() || !raiz.path("ok").isBoolean()) {
            throw new IOException("Resposta inesperada do Apps Script");
        }
        if (!raiz.path("ok").asBoolean()) {
            throw new IOException("Apps Script: " + raiz.path("error").asText("erro desconhecido"));
        }
        List<LeituraTelemetria> leituras = new ArrayList<>();
        adicionar(raiz, "temperature", TipoSensor.TEMPERATURA, "C", leituras);
        adicionar(raiz, "humidity", TipoSensor.UMIDADE, "%", leituras);
        adicionar(raiz, "luminosity", TipoSensor.LUMINOSIDADE, "ADC", leituras);
        return List.copyOf(leituras);
    }

    private static void adicionar(JsonNode raiz, String campo, TipoSensor tipo, String unidade,
                                  List<LeituraTelemetria> destino) throws IOException {
        JsonNode registros = raiz.path(campo);
        if (!registros.isArray()) throw new IOException("Campo ausente ou invalido: " + campo);
        for (JsonNode registro : registros) {
            try {
                JsonNode valor = registro.path("value");
                if (!valor.isNumber() || !Double.isFinite(valor.asDouble())) {
                    throw new IllegalArgumentException("valor invalido");
                }
                LocalDate data = LocalDate.parse(registro.path("date").asText());
                LocalTime hora = LocalTime.parse(registro.path("time").asText());
                destino.add(new LeituraTelemetria(tipo, valor.asDouble(), unidade,
                        LocalDateTime.of(data, hora)));
            } catch (RuntimeException ex) {
                throw new IOException("Leitura invalida em " + campo + ": " + ex.getMessage(), ex);
            }
        }
    }
}
