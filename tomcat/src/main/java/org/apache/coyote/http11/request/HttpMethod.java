package org.apache.coyote.http11.request;

import java.util.Arrays;

public enum HttpMethod {
    GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH;

    public static HttpMethod of(final String httpMethod) {
        return Arrays.stream(HttpMethod.values())
                .filter(value -> value.name().equalsIgnoreCase(httpMethod))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
