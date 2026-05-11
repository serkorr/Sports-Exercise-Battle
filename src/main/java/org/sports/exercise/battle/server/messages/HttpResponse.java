package org.sports.exercise.battle.server.messages;

import java.nio.charset.StandardCharsets;
import java.util.Map;

//source: https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Messages

public record HttpResponse(int statusCode,
                           String statusText,
                           Map<String, String> headers,
                           String body) {
    public HttpResponse{
        headers = headers == null ? Map.of() : Map.copyOf(headers);
        body = body == null ? "" : body;
        statusText = statusText == null ? "" : statusText;
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(200, "OK", Map.of(), body);
    }

    public static HttpResponse created(String body) {
        return new HttpResponse(201, "Created", Map.of(), body);
    }

    public static HttpResponse badRequest(String body) {
        return new HttpResponse(400, "Bad Request", Map.of(), body);
    }

    public static HttpResponse unauthorized(String body) {
        return new HttpResponse(401, "Unauthorized", Map.of(), body);
    }

    public static HttpResponse notFound(String body) {
        return new HttpResponse(404, "Not Found", Map.of(), body);
    }

    public static HttpResponse internalServerError(String body) {
        return new HttpResponse(500, "Internal Server Error", Map.of(), body);
    }

    public String toRawResponse(){

        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        StringBuilder response = new StringBuilder();

        response.append("HTTP/1.1 ")
                .append(statusCode)
                .append(" ")
                .append(statusText)
                .append("\r\n");

        response.append("Content-Length: ")
                .append(bodyBytes.length)
                .append("\r\n");

        response.append("Content-Type: application/json")
                .append("\r\n");

        for(Map.Entry<String, String> header : headers.entrySet()){

            //skip so we dont have duplicate of content length and type
            if(header.getKey().equalsIgnoreCase("Content-Length") || header.getKey().equalsIgnoreCase("Content-Type")){
                continue;
            }

            response.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }

        response.append("\r\n");
        response.append(body);

        return response.toString();
    }
}
