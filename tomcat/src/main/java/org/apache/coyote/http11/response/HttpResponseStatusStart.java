package org.apache.coyote.http11.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.http11.common.HttpVersion;

@Getter
@RequiredArgsConstructor
public class HttpResponseStatusStart {
    private final HttpVersion httpVersion;
    private final HttpStatus httpStatus;

    public static HttpResponseStatusStart of(final HttpVersion httpVersion, final HttpStatus httpStatus){
        return new HttpResponseStatusStart(httpVersion, httpStatus);
    }
}
