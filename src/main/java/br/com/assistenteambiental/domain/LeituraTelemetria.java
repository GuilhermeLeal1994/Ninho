package br.com.assistenteambiental.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class LeituraTelemetria {
    private final TipoSensor tipoSensor;
    private final double valor;
    private final String unidade;
    private final LocalDateTime dataHora;

    public LeituraTelemetria(TipoSensor tipoSensor, double valor, String unidade, LocalDateTime dataHora) {
        this.tipoSensor = Objects.requireNonNull(tipoSensor, "tipoSensor");
        if (!Double.isFinite(valor)) throw new IllegalArgumentException("valor deve ser finito");
        this.valor = valor;
        this.unidade = Objects.requireNonNull(unidade, "unidade");
        this.dataHora = Objects.requireNonNull(dataHora, "dataHora");
    }

    // Compatibilidade com os testes e chamadas da Semana 1.
    public LeituraTelemetria(String tipoSensor, double valor, String unidade, LocalDateTime dataHora) {
        this(TipoSensor.valueOf(Objects.requireNonNull(tipoSensor, "tipoSensor")), valor, unidade, dataHora);
    }

    public String getTipoSensor() { return tipoSensor.name(); }
    public TipoSensor getTipoSensorEnum() { return tipoSensor; }
    public double getValor() { return valor; }
    public String getUnidade() { return unidade; }
    public LocalDateTime getDataHora() { return dataHora; }

    @Override public String toString() {
        return tipoSensor + ": " + valor + " " + unidade + " - " + dataHora;
    }
}
