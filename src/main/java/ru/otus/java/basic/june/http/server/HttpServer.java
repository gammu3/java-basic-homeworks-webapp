package ru.otus.java.basic.june.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;
    private ExecutorService threadPool;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
        this.threadPool = Executors.newFixedThreadPool(10); // Пул из 10 потоков
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port + ". Ожидаем подключения");
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new ClientHandler(socket, dispatcher));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
