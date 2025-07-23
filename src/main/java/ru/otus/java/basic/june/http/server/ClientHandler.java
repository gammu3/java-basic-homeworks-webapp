package ru.otus.java.basic.june.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.io.ByteArrayOutputStream;

public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private static final int BUFFER_SIZE = 65536;

    private Socket socket;
    private Dispatcher dispatcher;

    public ClientHandler(Socket socket, Dispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try (OutputStream output = socket.getOutputStream();
             ByteArrayOutputStream requestBuffer = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
                requestBuffer.write(buffer, 0, bytesRead);
                if (bytesRead < BUFFER_SIZE) {
                    break;
                }
            }

            String rawRequest = requestBuffer.toString("UTF-8");
            if (!rawRequest.isEmpty()) {
                HttpRequest request = new HttpRequest(rawRequest);
                if (request.getMethod() != null && request.getUri() != null) {
                    logger.info("Request received: {} {}", request.getMethod(), request.getUri());
                } else {
                    logger.warn("Invalid request - method or URI is null");
                }
                dispatcher.execute(request, output);
            }
        } catch (IOException e) {
            logger.error("Error handling client request", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Error closing socket", e);
            }
        }
    }
}