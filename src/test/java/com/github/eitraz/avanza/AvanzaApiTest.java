package com.github.eitraz.avanza;

import com.github.eitraz.avanza.model.account.Overview;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.util.Objects.requireNonNull;

class AvanzaApiTest {
    private static AvanzaApi api;

    @BeforeAll
    static void setUpApi() {
        String totp = requireNonNull(System.getProperty("totp"), "System property 'totp' is required (using for example -Dtotp=[totp])");

        api = new AvanzaApi(
                requireNonNull(System.getProperty("username"), "System property 'username' is required (using for example -Dusername=[USERNAME])"),
                requireNonNull(System.getProperty("password"), "System property 'password' is required (using for example -Dpassword=[PASSWORD])"),
                () -> totp
        );
    }

    @Test
    void testOverview() {
        Overview overview = api.getOverview();

        overview.getAccounts().forEach(account -> {
            api.getAccountOverview(account.getAccountId());
        });
    }
}