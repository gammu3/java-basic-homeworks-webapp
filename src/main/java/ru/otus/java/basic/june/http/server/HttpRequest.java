package ru.otus.java.basic.june.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private String rawRequest;
    private String method;
    private String uri;
    private Map<String, String> parameters;
    private String body;

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getBody() {
        return body;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parameters = new HashMap<>();
        parse();
    }

    public void info(boolean showRawRequest) {
        if (showRawRequest) {
            System.out.println(rawRequest);
        }
        System.out.println("METHOD: " + method);
        System.out.println("URI: " + uri);
        System.out.println("BODY: " + body);
    }

    private void parse() {
        try {
            int startIndex = rawRequest.indexOf(' ');
            int endIndex = rawRequest.indexOf(' ', startIndex + 1);
            if (startIndex > 0 && endIndex > startIndex) {
                method = rawRequest.substring(0, startIndex);
                uri = rawRequest.substring(startIndex + 1, endIndex);

                if (uri.contains("?")) {
                    String[] elements = uri.split("[?]");
                    uri = elements[0];
                    String[] keysValues = elements[1].split("[&]");
                    for (String o : keysValues) {
                        String[] keyValue = o.split("=");
                        if (keyValue.length == 2) {
                            parameters.put(keyValue[0], keyValue[1]);
                        }
                    }
                }

                int bodyStart = rawRequest.indexOf("\r\n\r\n");
                if (bodyStart > 0) {
                    body = rawRequest.substring(bodyStart + 4);
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing HTTP request", e);
            method = null;
            uri = null;
        }
    }
}










