package org.sports.exercise.battle;

import org.sports.exercise.battle.server.HttpConnectionHandler;
import org.sports.exercise.battle.server.HttpRequestParser;
import org.sports.exercise.battle.server.ServerRequestHandler;
import org.sports.exercise.battle.server.TCPServer;
import org.sports.exercise.battle.server.router.RouteConfig;
import org.sports.exercise.battle.server.router.Router;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        Router router = RouteConfig.createRouter();
        ServerRequestHandler serverRequestHandler = new ServerRequestHandler(router);
        HttpRequestParser httpRequestParser = new HttpRequestParser();
        HttpConnectionHandler httpConnectionHandler = new HttpConnectionHandler(serverRequestHandler, httpRequestParser);
        TCPServer server = new TCPServer(8080, httpConnectionHandler);

        server.start();
    }
}