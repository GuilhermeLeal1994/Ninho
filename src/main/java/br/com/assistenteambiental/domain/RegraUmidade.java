package br.com.assistenteambiental.domain;

public class RegraUmidade implements RegraAnomalia {
    @Override public TipoSensor tipoSensor() { return TipoSensor.UMIDADE; }
    @Override public boolean ehAnomalia(double valor) { return valor < 30.0; }
    @Override public String mensagem() { return "Umidade abaixo do limite"; }
}
