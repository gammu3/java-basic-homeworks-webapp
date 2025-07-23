package ru.otus.java.basic.june.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private int port;
    private Dispatcher dispatcher;
    private ExecutorService threadPool;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port {}", port);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    logger.debug("New connection from {}", socket.getInetAddress());
                    threadPool.execute(new ClientHandler(socket, dispatcher));
                } catch (IOException e) {
                    logger.error("Error accepting client connection", e);
                }
            }
        } catch (IOException e) {
            logger.error("Server error", e);
        } finally {
            threadPool.shutdown();
            logger.info("Server stopped");
        }
    }
}