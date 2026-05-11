package org.sports.exercise.battle.server;

import org.sports.exercise.battle.server.exceptions.BadRequestException;
import org.sports.exercise.battle.server.messages.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    public HttpRequest parse(String rawRequest){
        String[] parts = rawRequest.split("\r\n\r\n", 2);

        String headerPart = parts[0];

        String body = parts.length > 1 ? parts[1] : "";

        String[] lines = headerPart.split("\r\n");

        String[] requestLine = lines[0].split(" ");

        if(requestLine.length < 2){
            throw new BadRequestException("Invalid request line");
        }

        String method = requestLine[0];
        String path = requestLine[1];

        Map<String, String> headers = getHeaders(lines);

        //convert into HttpRequest
        return new HttpRequest(method, path, headers, body);
    }

    private Map<String, String> getHeaders(String[] lines) {
        Map<String, String> headers = new HashMap<>();

        for (int i = 0; i < lines.length; ++i){
            String line = lines[i];
            //example: Content-Type: application/json
            int colonIndex = line.indexOf(":");

            //edge case --> first header wont have colonIndex it will be like //POST /login HTTP/1.1 --> so colonIndex will be -1
            if(colonIndex == -1){
                //just skip to next line
                continue;
            }

            //gets for example Content-Type
            String name = line.substring(0, colonIndex).trim();

            //gets for example the value of Content-Type
            //+1 because of the whitespace
            String value = line.substring(colonIndex + 1).trim();

            headers.put(name, value);
        }
        return headers;
    }
}
