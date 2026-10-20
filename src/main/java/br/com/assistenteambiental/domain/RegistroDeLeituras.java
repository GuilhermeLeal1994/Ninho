package br.com.assistenteambiental.domain;

/** Porta do domínio para registrar cada leitura sem conhecer arquivos. */
public interface RegistroDeLeituras {
    /** Registra uma leitura recebida pelo processamento. */
    void registrar(LeituraTelemetria leitura);
}
