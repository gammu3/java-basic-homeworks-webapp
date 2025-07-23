package ru.otus.java.basic.june.http.server.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.june.http.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public class HelloRequestProcessor implements RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(HelloRequestProcessor.class);

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        logger.info("Processing hello request");
        String htmlResponse = "<html><body>" +
                "<h1>Hello World</h1>" +
                "<p>Hello</p>" +
                "<h2>Hello World</h2>" +
                "</body></html>";
        output.write(createHtmlResponse(200, htmlResponse).getBytes());
    }
}