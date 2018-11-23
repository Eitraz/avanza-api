package com.github.eitraz.avanza.model.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private TwoFactorLogin twoFactorLogin;
}
