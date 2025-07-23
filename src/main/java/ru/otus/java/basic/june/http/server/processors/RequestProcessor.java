package ru.otus.java.basic.june.http.server.processors;

import ru.otus.java.basic.june.http.server.HttpRequest;
import ru.otus.java.basic.june.http.server.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    void execute(HttpRequest request, OutputStream output) throws IOException;

    default HttpResponse createResponse(int statusCode, String contentType, String content) {
        return new HttpResponse(statusCode, contentType, content);
    }

    default HttpResponse createJsonResponse(int statusCode, String jsonContent) {
        return new HttpResponse(statusCode, "application/json", jsonContent);
    }

    default HttpResponse createHtmlResponse(int statusCode, String htmlContent) {
        return new HttpResponse(statusCode, "text/html", htmlContent);
    }
}