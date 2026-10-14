package br.com.assistenteambiental.domain;

public interface RegraAnomalia {
    TipoSensor tipoSensor();
    boolean ehAnomalia(double valor);
    String mensagem();
}
