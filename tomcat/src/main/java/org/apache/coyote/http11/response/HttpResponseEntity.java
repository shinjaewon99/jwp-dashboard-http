package org.apache.coyote.http11.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class HttpResponseEntity {

    private final HttpStatus httpStatus;
    private final String requestTarget;
    private final String responseBody;
    private final ResponsePage responsePage;

}
