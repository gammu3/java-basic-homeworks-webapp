package ru.otus.java.basic.june.http.server;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private int statusCode;
    private String contentType;
    private String content;

    public HttpResponse(int statusCode, String contentType, String content) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.content = content;
    }

    public byte[] getBytes() {
        String statusText = "OK";
        switch (statusCode) {
            case 200: statusText = "OK"; break;
            case 400: statusText = "Bad Request"; break;
            case 404: statusText = "Not Found"; break;
            case 500: statusText = "Internal Server Error"; break;
        }

        String response = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + content.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "\r\n" +
                content;
        return response.getBytes(StandardCharsets.UTF_8);
    }
}