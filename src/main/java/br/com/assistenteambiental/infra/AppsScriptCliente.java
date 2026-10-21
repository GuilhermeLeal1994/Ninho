package br.com.assistenteambiental.infra;

import br.com.assistenteambiental.domain.LeituraTelemetria;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/** Cliente de consulta, sem expor a chave nos logs. */
public final class AppsScriptCliente {
    private final HttpClient cliente;
    private final URI endpoint;

    public AppsScriptCliente(String urlExec, String chave) {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL).build(), urlExec, chave);
    }

    public AppsScriptCliente(HttpClient cliente, String urlExec, String chave) {
        if (urlExec == null || !urlExec.startsWith("https://script.google.com/macros/s/") ||
                !urlExec.matches("https://script\\.google\\.com/macros/s/[^?#]+/exec/?")) {
            throw new IllegalArgumentException("Informe a URL HTTPS /exec do Apps Script");
        }
        if (chave == null || chave.isBlank()) throw new IllegalArgumentException("Chave ausente");
        this.cliente = java.util.Objects.requireNonNull(cliente);
        this.endpoint = URI.create(urlExec + "?key=" +
                URLEncoder.encode(chave, StandardCharsets.UTF_8));
    }

    public List<LeituraTelemetria> consultar() throws IOException, InterruptedException {
        HttpRequest pedido = HttpRequest.newBuilder(endpoint)
                .timeout(Duration.ofSeconds(30)).GET().build();
        HttpResponse<String> resposta = cliente.send(pedido, HttpResponse.BodyHandlers.ofString());
        if (resposta.statusCode() != 200) {
            throw new IOException("Falha HTTP ao consultar Apps Script: " + resposta.statusCode());
        }
        return AppsScriptLeituras.interpretar(resposta.body());
    }
}
