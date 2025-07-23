package ru.otus.java.basic.june.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.june.http.server.app.ItemsRepository;
import ru.otus.java.basic.june.http.server.exceptions_handling.BadRequestException;
import ru.otus.java.basic.june.http.server.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private Map<String, RequestProcessor> routes;
    private RequestProcessor defaultNotFountRequestProcessor;
    private RequestProcessor defaultStaticResourcesRequestProcessor;

    public Dispatcher() {
        this.routes = new HashMap<>();
        ItemsRepository itemsRepository = new ItemsRepository();
        this.routes.put("GET /hello", new HelloRequestProcessor());
        this.routes.put("GET /calc", new CalcRequestProcessor());
        this.routes.put("GET /items", new GetItemsRequestProcessor(itemsRepository));
        this.routes.put("POST /items", new CreateItemRequestProcessor(itemsRepository));
        this.defaultNotFountRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.defaultStaticResourcesRequestProcessor = new DefaultStaticResourcesProcessor();

        logger.info("Dispatcher initialized with {} routes", routes.size());
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        logger.debug("Dispatching request: {}", request.getRoutingKey());

        try {
            if (Files.exists(Paths.get("static/", request.getUri().substring(1)))) {
                defaultStaticResourcesRequestProcessor.execute(request, output);
                return;
            }

            RequestProcessor processor = routes.get(request.getRoutingKey());
            if (processor == null) {
                defaultNotFountRequestProcessor.execute(request, output);
                return;
            }

            processor.execute(request, output);
        } catch (BadRequestException e) {
            logger.warn("Bad request: {} - {}", e.getCode(), e.getDescription());
            byte[] response = new HttpResponse(400, "text/html; charset=utf-8",
                    "<html><body><h1>Bad Request</h1><p>" +
                            e.getCode() + ": " + e.getDescription() + "</p></body></html>")
                    .getBytes();
            output.write(response);
        } catch (Exception e) {
            logger.error("Internal server error", e);
            byte[] response = new HttpResponse(500, "text/html; charset=utf-8",
                    "<html><body><h1>500 Internal Server Error</h1>" +
                            "<h5>Oops, something went wrong, please try again later...</h5></body></html>")
                    .getBytes();
            output.write(response);
        }
    }
}