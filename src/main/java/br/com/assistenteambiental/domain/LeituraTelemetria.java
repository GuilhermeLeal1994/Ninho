package br.com.assistenteambiental.domain;

import java.time.LocalDateTime;

public class LeituraTelemetria {

    private final String tipoSensor;
    private final double valor;
    private final String unidade;
    private final LocalDateTime dataHora;

    public LeituraTelemetria(
            String tipoSensor,
            double valor,
            String unidade,
            LocalDateTime dataHora) {

        this.tipoSensor = tipoSensor;
        this.valor = valor;
        this.unidade = unidade;
        this.dataHora = dataHora;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public double getValor() {
        return valor;
    }

    public String getUnidade() {
        return unidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        return tipoSensor +
                ": " +
                valor +
                " " +
                unidade +
                " - " +
                dataHora;
    }
}