package br.com.assistenteambiental.domain;

import java.util.List;

/** Porta do domínio para persistência de alertas; não conhece JDBC ou SQLite. */
public interface AlertaRepository {
    /** Salva um alerta identificado pelo analisador. */
    void salvar(Alerta alerta);

    /** Retorna os alertas persistidos. */
    List<Alerta> listarTodos();
}
