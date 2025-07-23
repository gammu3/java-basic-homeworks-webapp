package ru.otus.java.basic.june.http.server.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.june.http.server.HttpRequest;
import ru.otus.java.basic.june.http.server.app.Item;
import ru.otus.java.basic.june.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetItemsRequestProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public GetItemsRequestProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        String response;

        if (request.getParameter("id") != null) {
            Long id = (Long) Long.parseLong(request.getParameter("id"));
            Item item = itemsRepository.get(id);
            if (item == null) {
                response = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: application/json\r\n" +
                        "\r\n" +
                        "{\"error\":\"Item not found\"}";
            } else {
                String itemJson = gson.toJson(item);
                response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json\r\n" +
                        "\r\n" +
                        itemJson;
            }
        } else {
            List<Item> items = itemsRepository.getAll();
            String itemsJson = gson.toJson(items);
            response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    itemsJson;
        }

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
