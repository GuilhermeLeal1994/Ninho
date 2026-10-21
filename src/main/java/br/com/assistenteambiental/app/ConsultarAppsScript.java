package br.com.assistenteambiental.app;

import br.com.assistenteambiental.domain.AnalisadorDeAnomalias;
import br.com.assistenteambiental.domain.LeituraTelemetria;
import br.com.assistenteambiental.infra.AppsScriptCliente;
import java.util.Comparator;
import java.util.List;

/** Execucao separada da simulacao concorrente Main. */
public final class ConsultarAppsScript {
    private ConsultarAppsScript() {}

    public static void main(String[] args) throws Exception {
        String url = System.getenv("NINHO_APPS_SCRIPT_URL");
        String chave = System.getenv("NINHO_APPS_SCRIPT_KEY");
        if (url == null || chave == null) {
            throw new IllegalStateException("Configure NINHO_APPS_SCRIPT_URL e NINHO_APPS_SCRIPT_KEY");
        }
        List<LeituraTelemetria> historico = new AppsScriptCliente(url, chave).consultar();
        AnalisadorDeAnomalias analisador = new AnalisadorDeAnomalias();
        System.out.println("Leituras recebidas: " + historico.size());
        historico.stream().collect(java.util.stream.Collectors.groupingBy(
                LeituraTelemetria::getTipoSensorEnum)).forEach((tipo, leituras) -> {
            LeituraTelemetria ultima = leituras.stream()
                    .max(Comparator.comparing(LeituraTelemetria::getDataHora)).orElseThrow();
            System.out.println(ultima);
            if (analisador.temAnomalia(ultima)) {
                System.out.println("ALERTA: " + analisador.analisar(ultima).getMensagem());
            }
        });
    }
}
