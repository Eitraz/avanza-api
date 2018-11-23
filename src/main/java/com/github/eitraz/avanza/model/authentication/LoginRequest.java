package com.github.eitraz.avanza.model.authentication;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class LoginRequest {
    private long maxInactiveMinutes;
    private String username;
    private String password;
}
