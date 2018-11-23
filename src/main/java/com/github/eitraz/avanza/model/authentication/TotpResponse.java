package com.github.eitraz.avanza.model.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotpResponse {
    private String authenticationSession;
    private String pushSubscriptionId;
    private Boolean registrationComplete;
    private String customerId;
}
