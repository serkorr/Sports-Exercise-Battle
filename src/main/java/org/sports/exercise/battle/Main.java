package org.sports.exercise.battle;

import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;
import org.sports.exercise.battle.infrastructure.database.DatabaseInitializer;
import org.sports.exercise.battle.server.HttpConnectionHandler;
import org.sports.exercise.battle.server.HttpRequestParser;
import org.sports.exercise.battle.server.ServerRequestHandler;
import org.sports.exercise.battle.server.TCPServer;
import org.sports.exercise.battle.server.router.RouteConfig;
import org.sports.exercise.battle.server.router.Router;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final int PORT = 10001;

    public static void main(String[] args) throws IOException {
        Router router = RouteConfig.createRouter();
        ServerRequestHandler serverRequestHandler = new ServerRequestHandler(router);
        HttpRequestParser httpRequestParser = new HttpRequestParser();
        HttpConnectionHandler httpConnectionHandler = new HttpConnectionHandler(serverRequestHandler, httpRequestParser);
        TCPServer server = new TCPServer(PORT, httpConnectionHandler);

        //initialize Database and create tables before starting the server
        try(Connection connection = DatabaseConfig.getConnection()){
            DatabaseInitializer.initialize(DatabaseConfig.getConnection());
        }catch(SQLException e){
            throw new RuntimeException("Could not initialize database, e");
        }

        server.start();
    }
}