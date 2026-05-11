package org.sports.exercise.battle.server;

import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

//handle one TCP Client
//reads raw request from socket
//converts into HttpRequest
//gets an HttpResponse from ServerRequestHandler
//converts the HttpResponse into byte format UTF_8
//writes raw response to socket

public class  HttpConnectionHandler {
    private final ServerRequestHandler serverRequestHandler;
    private static final Logger logger = Logger.getLogger(HttpConnectionHandler.class.getName());
    private final HttpRequestParser httpRequestParser;

    public HttpConnectionHandler(ServerRequestHandler serverRequestHandler, HttpRequestParser httpRequestParser) {
        this.serverRequestHandler = serverRequestHandler;
        this.httpRequestParser = httpRequestParser;
    }

    public void handle(Socket clientSocket){
        try(clientSocket){
            logger.info("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            String rawRequest = readRawRequest(clientSocket);

            //converting into HttpRequest
            HttpRequest httpRequest = httpRequestParser.parse(rawRequest);

            HttpResponse httpResponse = serverRequestHandler.handle(httpRequest);

            byte[] rawResponse = httpResponse.toRawResponse().getBytes(StandardCharsets.UTF_8);

            //send back to the client the raw response
            clientSocket.getOutputStream().write(rawResponse);

            clientSocket.getOutputStream().flush();

        }catch(IOException e){
            logger.severe("Error handling client: " + e.getMessage());
        }
    }

    private String readRawRequest(Socket clientSocket) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;

        //use \r\n at the end of a line, because http expects CRLF line endings
        while((line = br.readLine()) != null){
            sb.append(line).append("\r\n");

            if(line.isEmpty()){
                break;
            }
        }

        //now we have something like
        //POST /login HTTP/1.1
        //Content-Type: application/json
        //Content-Length: 38

        //{"username": "serkan", "password": "1234"}

        //get the content length for our body request
        int contentLength = getContentLength(sb.toString());

        //now we need to read the content
        if(contentLength > 0){
            char[] bodyChars = new char[contentLength];

            //read up the contentlength chars from the socket
            int read = br.read(bodyChars, 0, contentLength);

            if(read > 0){
                //store them inside the bodychars starting at idx 0
                sb.append(new String(bodyChars, 0, read));
            }
        }
        return sb.toString();
    }

    private int getContentLength(String rawHeaders){
        String[] lines = rawHeaders.split("\r\n");

        for(String line: lines){
            if(line.toLowerCase().startsWith("content-length:")){
                //takes only the number, so everything after content-length:
                return Integer.parseInt(line.substring("content-length:".length()).trim());
            }
        }

        return 0;
    }
}
