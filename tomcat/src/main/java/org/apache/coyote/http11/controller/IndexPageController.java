package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.*;

import java.io.IOException;

import static org.apache.coyote.http11.response.ResponsePage.EMPTY;

public class IndexPageController implements Controller {


    @Override
    public HttpResponseEntity service(final HttpRequest httpRequest) throws IOException {
        final String requestTarget = httpRequest.getHttpRequestStartLine().getPath();

        final var responseBody = "Hello world!";

        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.OK)
                .contentType(generateContentType(requestTarget))
                .responseBody(HttpResponseBody.from(responseBody))
                .responsePage(EMPTY)
                .build();
    }
}
