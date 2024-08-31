package org.apache.coyote.http11.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HttpResponseEntity {

    private final HttpStatus httpStatus;
    private final String requestTarget;
    private final String responseBody;

    public static HttpResponseEntity of(final HttpStatus httpStatus, final String requestTarget, final String responseBody) {
        return new HttpResponseEntity(httpStatus, requestTarget, responseBody);
    }
}
