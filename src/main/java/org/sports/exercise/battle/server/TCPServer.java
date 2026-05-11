package org.sports.exercise.battle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPServer {
    private final int PORT;
    private final HttpConnectionHandler httpConnectionHandler;
    private final Logger logger = Logger.getLogger(TCPServer.class.getName());

    public TCPServer(int port, HttpConnectionHandler httpConnectionHandler){
        this.PORT = port;
        this.httpConnectionHandler = httpConnectionHandler;
    }

    public void start() throws IOException{
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server listening on http://localhost:" + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                httpConnectionHandler.handle(clientSocket);
            }
        }
    }
}
