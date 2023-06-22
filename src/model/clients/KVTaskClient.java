package model.clients;

import model.utils.Converter;

import java.nio.charset.StandardCharsets;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KVTaskClient {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8056;
    private final String url;
    private final HttpClient httpClient;
    private String token;
    private final Logger logger = LoggerFactory.getLogger(KVTaskClient.class);

    public KVTaskClient() {
        url = "http://localhost:" + PORT;
        token = registerAPIToken(url);
        Converter.createGson();
        httpClient = HttpClient.newHttpClient();
    }

    private String registerAPIToken(String url) {
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            token = response.body();
        } catch (InterruptedException | IOException e) {
            logger.error("Регистрация API токена закончилась неудачно. Причина: {}", e.getMessage());
        }
        return token;
    }

    public void save(String key, String value) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token);
        var body = HttpRequest.BodyPublishers.ofString(value, DEFAULT_CHARSET);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            logger.error("Сохранение закончилось неудачно. Причина: {}", e.getMessage());
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            logger.error("Загрузка закончилась неудачно. Причина: {}", e.getMessage());
        }
        return response != null ? response.body() : "KVTaskClient.load() is greeting you";
    }
}
