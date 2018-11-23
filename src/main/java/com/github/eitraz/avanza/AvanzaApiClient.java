package com.github.eitraz.avanza;

import com.github.eitraz.avanza.http.HttpMethod;
import com.github.eitraz.avanza.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class AvanzaApiClient {
    private static final Logger logger = LoggerFactory.getLogger(AvanzaApiClient.class);

    private static final String BASE_URL = "https://www.avanza.se/";

    private final TotpAuthentication authentication;

    AvanzaApiClient(TotpAuthentication authentication) {
        this.authentication = authentication;
    }

    /**
     * Authenticated GET
     */
    <T> T get(String path, Class<T> responseType) {
        return doRequest(HttpMethod.GET, path, authentication.getHeaders(this), null, responseType).getBody();
    }

    /**
     * Authenticated POST
     */
    @SuppressWarnings("unused")
    <T> T post(String path, Object data, Class<T> responseType) {
        return doRequest(HttpMethod.POST, path, authentication.getHeaders(this), data, responseType).getBody();
    }

    synchronized <T> HttpResponse<T> doRequest(HttpMethod method, String path, Map<String, String> headers, Object dataObject, Class<T> responseType) {
        String data = dataObject != null ? jsonObjectToString(dataObject) : null;

        headers = new HashMap<>(ofNullable(headers).orElseGet(HashMap::new));

        // Default headers
        headers.put("User-Agent", "Avanza API client");
        headers.put("Accept", "*/*");
        headers.put("Content-Type", "application/json; charset=UTF-8");

        HttpURLConnection connection = openConnection(getUrl(path));
        try {

            HttpMethod.setRequestMethod(connection, method);

            // Set headers
            headers.forEach(connection::setRequestProperty);

            logRequest(connection, data);

            if (data != null) {
                setOutput(connection, data);
            }

            int responseCode = getResponseCode(connection);
            String responseBody = getResponseBody(connection, responseCode);

            logResponse(connection, responseCode, responseBody);

            // Valid response code
            if (responseCode >= 200 && responseCode <= 299) {
                return new HttpResponse<>(
                        getResponseHeaders(connection.getHeaderFields()),
                        stringToJsonObject(responseBody, responseType));
            }
            // Invalid response code
            else {
                if (responseCode == 401) {
                    authentication.invalidateSession();
                }

                throw new RuntimeException(String.format("Request for %s failed with status %d and body: %s",
                        connection.getURL().toString(),
                        responseCode,
                        responseBody));
            }
        } finally {
            connection.disconnect();
        }
    }

    private Map<String, String> getResponseHeaders(Map<String, List<String>> headerFields) {
        return headerFields.entrySet().stream()
                           .collect(Collectors.toMap(
                                   Map.Entry::getKey,
                                   o -> o.getValue().stream().collect(joining(","))
                           ));
    }

    private void setOutput(HttpURLConnection connection, String data) {
        connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
        connection.setDoOutput(true);

        try {
            IOUtils.write(data, connection.getOutputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to HTTP output stream", e);
        }
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    private <T> T stringToJsonObject(String string, Class<T> type) {
        if (string == null)
            return null;

        try {
            return getObjectMapper().readValue(string, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create " + type.getName() + " object from JSON string", e);
        }
    }

    private String jsonObjectToString(Object data) {
        if (data == null)
            return null;

        try {
            return getObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create JSON string from object", e);
        }
    }

    @SuppressWarnings("Duplicates")
    private URL getUrl(String path) {
        try {
            // Remove leading '/'
            while (path.startsWith("/") && path.length() > 1) {
                path = path.substring(1);
            }

            return new URL(BASE_URL + path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    private HttpURLConnection openConnection(URL url) {
        try {
            logger.debug("Opening connection for {}", url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            return connection;
        } catch (IOException e) {
            throw new RuntimeException("Failed to open HTTP connection", e);
        }
    }

    private int getResponseCode(HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get HTTP response code", e);
        }
    }

    private String getResponseBody(HttpURLConnection connection, int responseCode) {
        try (InputStream inputStream = getResponseInputStream(connection, responseCode)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read response input stream", e);
        }
    }

    private InputStream getResponseInputStream(HttpURLConnection connection, int responseCode) {
        try {
            return (responseCode >= 200 && responseCode <= 299) ? connection.getInputStream() : connection.getErrorStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain response input stream", e);
        }
    }

    private void logRequest(HttpURLConnection connection, String data) {
        logger.info(">> Request {} {}{}",
                connection.getRequestMethod(),
                connection.getURL().toString(),
                ofNullable(data).map(body -> String.format(" with request body: %s", body))
                                .orElse(""));

        connection.getRequestProperties()
                  .forEach((key, value) -> logger.debug(">> Request header: {} = {}", key, value));
    }

    private void logResponse(HttpURLConnection connection, int responseCode, String responseBody) {
        logger.info("<< Response for {} {} with status code {} and body: {}",
                connection.getRequestMethod(),
                connection.getURL().toString(),
                responseCode,
                responseBody);

        connection.getHeaderFields()
                  .forEach((key, value) -> logger.debug("<< Response header: {} = {}", key, value));
    }
}
