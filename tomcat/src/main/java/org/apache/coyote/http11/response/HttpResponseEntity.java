package org.apache.coyote.http11.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.http11.cookie.HttpCookie;

@Getter
@Builder
@RequiredArgsConstructor
public class HttpResponseEntity {

    private final HttpStatus httpStatus;
    private final String requestTarget;
    private final String responseBody;

    @Builder.Default
    private final ResponsePage responsePage  = ResponsePage.empty();

    @Builder.Default
    private final HttpCookie httpCookie = HttpCookie.empty();

    public void setCookie(final String key, final String value) {
        httpCookie.put(key, value);
    }
}
