package org.apache.coyote.http11.request;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST;


    public static HttpMethod of(final String httpMethod) {
        return Arrays.stream(HttpMethod.values())
                .filter(value -> value.name().equalsIgnoreCase(httpMethod))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
