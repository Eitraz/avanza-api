# Avanza Bank

Unofficial Java API client for the unofficial Avanza API.

## Authentication
- Username and password with TOTP code

## Features
- Overview
- Account overview

## Code examples

```java
// Get overview and details for all accounts
AvanzaApi api = new AvanzaApi(username, password, () -> totp);

// Get overview
Overview overview = api.getOverview();
System.out.println("Total own capital: " + overview.getTotalOwnCapital());

// Get details about all accounts
overview.getAccounts().forEach(account -> {
    AccountOverview accountOverview = api.getAccountOverview(account.getAccountId());
    System.out.println(accountOverview.getAccountId() + " total profit: " + accountOverview.getTotalProfit());
});
```

## Thanks
Based on the work made by https://github.com/fhqvst/avanza.