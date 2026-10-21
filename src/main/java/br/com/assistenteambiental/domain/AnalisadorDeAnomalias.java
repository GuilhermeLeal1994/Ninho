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

    /** ADC e lux sao grandezas diferentes; nao aplicar limiar em lux ao ADC. */
    private boolean ehAnomalia(RegraAnomalia regra, LeituraTelemetria leitura) {
        if (leitura.getTipoSensorEnum() == TipoSensor.LUMINOSIDADE
                && "ADC".equalsIgnoreCase(leitura.getUnidade())) {
            double valor = leitura.getValor();
            if (valor != Math.rint(valor) || valor < 0 || valor > 4095) {
                throw new IllegalArgumentException("ADC deve ser inteiro entre 0 e 4095");
            }
            return ClassificacaoLuminosidade.classificar((int) valor)
                    == ClassificacaoLuminosidade.ESCURO;
        }
        return regra.ehAnomalia(leitura.getValor());
    }

    public boolean temAnomalia(LeituraTelemetria leitura) {
        RegraAnomalia regra = encontrarRegra(leitura);
        return regra != null && ehAnomalia(regra, leitura);
    }

    public Alerta analisar(LeituraTelemetria leitura) {
        RegraAnomalia regra = encontrarRegra(leitura);
        if (regra == null || !ehAnomalia(regra, leitura)) {
            return null;
        }
        return new Alerta(leitura.getTipoSensor(), leitura.getValor(),
                regra.mensagem(), leitura.getDataHora());
    }
}
