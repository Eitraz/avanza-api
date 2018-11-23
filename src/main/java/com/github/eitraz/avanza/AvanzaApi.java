package com.github.eitraz.avanza;

import com.github.eitraz.avanza.model.account.AccountOverview;
import com.github.eitraz.avanza.model.account.Overview;

import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public class AvanzaApi {
    private final AvanzaApiClient client;

    public AvanzaApi(String username, String password, Supplier<String> totpSupplier) {
        this.client = new AvanzaApiClient(new TotpAuthentication(username, password, totpSupplier));
    }

    public Overview getOverview() {
        return client.get("/_mobile/account/overview", Overview.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public AccountOverview getAccountOverview(String accountId) {
        return client.get("/_mobile/account/" + accountId + "/overview", AccountOverview.class);
    }
}
