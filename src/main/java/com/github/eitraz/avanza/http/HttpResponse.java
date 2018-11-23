package com.github.eitraz.avanza.http;

import java.util.Map;

public class HttpResponse<T> {
    private final Map<String, String> headers;
    private final T body;

    public HttpResponse(Map<String, String> headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }
}
