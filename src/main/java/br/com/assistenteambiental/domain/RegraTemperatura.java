package br.com.assistenteambiental.domain;

public class RegraTemperatura implements RegraAnomalia {
    @Override public TipoSensor tipoSensor() { return TipoSensor.TEMPERATURA; }
    @Override public boolean ehAnomalia(double valor) { return valor > 30.0; }
    @Override public String mensagem() { return "Temperatura acima do limite"; }
}
