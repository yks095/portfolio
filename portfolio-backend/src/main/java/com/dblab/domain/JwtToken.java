package com.dblab.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class JwtToken implements Serializable {
    private final String jwtToken;

    public JwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}
