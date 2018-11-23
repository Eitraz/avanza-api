package com.github.eitraz.avanza;

import com.github.eitraz.avanza.http.HttpMethod;
import com.github.eitraz.avanza.http.HttpResponse;
import com.github.eitraz.avanza.model.authentication.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;

public class TotpAuthentication {
    private static final String TOTP = "TOTP";

    private final String username;
    private final String password;
    private final Supplier<String> totpSupplier;

    private Map<String, String> headers = new HashMap<>();

    @SuppressWarnings("WeakerAccess")
    public TotpAuthentication(String username, String password, Supplier<String> totpSupplier) {
        this.username = username;
        this.password = password;
        this.totpSupplier = totpSupplier;
    }

    private void login(AvanzaApiClient client) {
        // Already logged in
        if (!headers.isEmpty()) {
            return;
        }

        // Get totp code before calling login, if used in GUI etc
        String totp = totpSupplier.get();

        // Login
        LoginResponse loginResponse = client.doRequest(
                HttpMethod.POST,
                "/_api/authentication/sessions/usercredentials",
                Collections.emptyMap(),
                new LoginRequest()
                        .setMaxInactiveMinutes(30)
                        .setUsername(username)
                        .setPassword(password),
                LoginResponse.class
        ).getBody();

        String method = ofNullable(loginResponse.getTwoFactorLogin())
                .map(TwoFactorLogin::getMethod)
                .orElse("");

        if (!TOTP.equalsIgnoreCase(method)) {
            throw new RuntimeException("Unsupported login second factor method " + method);
        }

        String transactionId = ofNullable(loginResponse.getTwoFactorLogin())
                .map(TwoFactorLogin::getTransactionId)
                .orElse("");

        totp(client, transactionId, totp);
    }

    private void totp(AvanzaApiClient client, String transactionId, String totp) {
        // TOTP request
        HttpResponse<TotpResponse> totpResponse = client.doRequest(
                HttpMethod.POST,
                "/_api/authentication/sessions/totp",
                singletonMap("Cookie", String.format("AZAMFATRANSACTION=%s", transactionId)),
                new TotpRequest()
                        .setMethod(TOTP)
                        .setTotpCode(totp),
                TotpResponse.class);

        // Response headers and body
        Map<String, String> responseHeaders = totpResponse.getHeaders();
        TotpResponse responseBody = totpResponse.getBody();

        String securityToken = responseHeaders.get("X-SecurityToken");
        String authenticationSession = responseBody.getAuthenticationSession();
//        String pushSubscriptionId = responseBody.getPushSubscriptionId();
//        String customerId = responseBody.getCustomerId();

        // New auth header
        this.headers = new HashMap<>();
        this.headers.put("X-AuthenticationSession", authenticationSession);
        this.headers.put("X-SecurityToken", securityToken);
    }

    public void invalidateSession() {
        headers = new HashMap<>();
    }

    Map<String, String> getHeaders(AvanzaApiClient client) {
        login(client);
        return this.headers;
    }
}
