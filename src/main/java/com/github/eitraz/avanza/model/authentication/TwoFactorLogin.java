package com.github.eitraz.avanza.model.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorLogin {
    private String transactionId;
    private String method;
}
