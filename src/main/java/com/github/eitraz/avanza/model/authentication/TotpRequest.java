package com.github.eitraz.avanza.model.authentication;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class TotpRequest {
    private String method = "TOTP";
    private String totpCode;
}
