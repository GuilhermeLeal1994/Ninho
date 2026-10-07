package br.com.assistenteambiental.domain;

import java.time.LocalDateTime;

public class Alerta {

    private final String tipoSensor;
    private final double valor;
    private final String mensagem;
    private final LocalDateTime dataHora;

    public Alerta(
            String tipoSensor,
            double valor,
            String mensagem,
            LocalDateTime dataHora) {

        this.tipoSensor = tipoSensor;
        this.valor = valor;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public double getValor() {
        return valor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        return "ALERTA - " +
                tipoSensor +
                ": " +
                valor +
                " - " +
                mensagem +
                " - " +
                dataHora;
    }
}