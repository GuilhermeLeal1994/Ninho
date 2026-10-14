package br.com.assistenteambiental.domain;

public class RegraLuminosidade implements RegraAnomalia {
    @Override public TipoSensor tipoSensor() { return TipoSensor.LUMINOSIDADE; }
    @Override public boolean ehAnomalia(double valor) { return valor < 100.0; }
    @Override public String mensagem() { return "Luminosidade abaixo do limite"; }
}
