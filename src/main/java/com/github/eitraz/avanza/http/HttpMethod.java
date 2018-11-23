package com.github.eitraz.avanza.http;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
public final class HttpMethod {
    public static final HttpMethod GET = new HttpMethod("GET");
    public static final HttpMethod POST = new HttpMethod("POST");
    public static final HttpMethod HEAD = new HttpMethod("HEAD");
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
    public static final HttpMethod PUT = new HttpMethod("PUT");
    public static final HttpMethod DELETE = new HttpMethod("DELETE");
    public static final HttpMethod TRACE = new HttpMethod("TRACE");

    private final String method;

    private HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public HttpURLConnection setRequestMethodForConnection(HttpURLConnection connection) {
        try {
            connection.setRequestMethod(getMethod());
            return connection;
        } catch (ProtocolException e) {
            throw new RuntimeException(String.format("Failed to set HTTP request method '%s'", getMethod()), e);
        }
    }

    public static HttpURLConnection setRequestMethod(HttpURLConnection connection, HttpMethod method) {
        return method.setRequestMethodForConnection(connection);
    }
}
