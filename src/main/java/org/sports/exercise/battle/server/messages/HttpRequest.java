package org.sports.exercise.battle.server.messages;

import java.util.Map;

//source: https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Messages

public record HttpRequest(
        String method,
        String path,
        Map<String, String> headers,
        String body
) {
    public HttpRequest {
        headers = headers == null ? Map.of() : Map.copyOf(headers);
        body = body == null ? "" : body;
    }

    public String header(String name) {
        return headers.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(name))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}