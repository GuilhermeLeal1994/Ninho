package br.com.assistenteambiental.domain;

public class AnalisadorDeAnomalias {

    public boolean temAnomalia(LeituraTelemetria leitura) {

        if ("TEMPERATURA".equals(leitura.getTipoSensor())) {
            return leitura.getValor() > 30.0;
        }

        if ("UMIDADE".equals(leitura.getTipoSensor())) {
            return leitura.getValor() < 30.0;
        }

        if ("LUMINOSIDADE".equals(leitura.getTipoSensor())) {
            return leitura.getValor() < 100.0;
        }

        return false;
    }

    public Alerta analisar(LeituraTelemetria leitura) {

        if (!temAnomalia(leitura)) {
            return null;
        }

        String mensagem;

        if ("TEMPERATURA".equals(leitura.getTipoSensor())) {
            mensagem = "Temperatura acima do limite";
        } else if ("UMIDADE".equals(leitura.getTipoSensor())) {
            mensagem = "Umidade abaixo do limite";
        } else if ("LUMINOSIDADE".equals(leitura.getTipoSensor())) {
            mensagem = "Luminosidade abaixo do limite";
        } else {
            mensagem = "Anomalia detectada";
        }

        return new Alerta(
                leitura.getTipoSensor(),
                leitura.getValor(),
                mensagem,
                leitura.getDataHora()
        );
    }
}