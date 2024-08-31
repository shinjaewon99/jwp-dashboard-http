package org.apache.coyote.http11.response;

import lombok.Getter;

@Getter
public enum HttpStatus {

    OK("200"),
    FOUND("302"),
    UNAUTHORIZED("401");

    private final String httpStatusCode;

    HttpStatus(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}