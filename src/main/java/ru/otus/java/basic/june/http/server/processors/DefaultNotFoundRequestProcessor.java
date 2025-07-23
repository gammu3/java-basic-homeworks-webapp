package ru.otus.java.basic.june.http.server.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.june.http.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DefaultNotFoundRequestProcessor implements RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DefaultNotFoundRequestProcessor.class);
    private static final String NOT_FOUND_IMAGE_PATH = "static/not_found.png";

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        logger.warn("Page not found: {}", request.getUri());

        try {
            byte[] imageData = Files.readAllBytes(Paths.get(NOT_FOUND_IMAGE_PATH));
            String response = "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: image/png\r\n" +
                    "Content-Length: " + imageData.length + "\r\n" +
                    "\r\n";
            output.write(response.getBytes());
            output.write(imageData);
        } catch (IOException e) {
            logger.error("Could not load 404 image, falling back to HTML", e);
            String htmlResponse = "<html><body>" +
                    "<h1>404 Not Found</h1>" +
                    "<p>The requested resource was not found on this server.</p>" +
                    "</body></html>";
            output.write(createHtmlResponse(404, htmlResponse).getBytes());
        }
    }
}