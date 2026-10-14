package br.com.assistenteambiental.domain;

import java.util.List;

public class AnalisadorDeAnomalias {
    private final List<RegraAnomalia> regras = List.of(
            new RegraTemperatura(), new RegraUmidade(), new RegraLuminosidade());

    private RegraAnomalia encontrarRegra(LeituraTelemetria leitura) {
        for (RegraAnomalia regra : regras) {
            if (regra.tipoSensor().name().equals(leitura.getTipoSensor())) {
                return regra;
            }
        }
        return null;
    }

    public boolean temAnomalia(LeituraTelemetria leitura) {
        RegraAnomalia regra = encontrarRegra(leitura);
        return regra != null && regra.ehAnomalia(leitura.getValor());
    }

    public Alerta analisar(LeituraTelemetria leitura) {
        RegraAnomalia regra = encontrarRegra(leitura);
        if (regra == null || !regra.ehAnomalia(leitura.getValor())) {
            return null;
        }
        return new Alerta(leitura.getTipoSensor(), leitura.getValor(),
                regra.mensagem(), leitura.getDataHora());
    }
}
