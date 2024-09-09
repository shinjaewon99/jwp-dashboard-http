package org.apache.coyote.http11.request;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HttpRequestStartLine {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestStartLine.class);
    private static final String BLANK = " ";
    private final HttpMethod httpMethod;
    private final String requestTarget;
    private final String httpVersion;

    public HttpRequestStartLine(HttpMethod httpMethod, String requestTarget, String httpVersion) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestStartLine from(final String httpRequest) throws IOException {
        List<String> requestStartLine = Arrays.stream(httpRequest.split(BLANK)).collect(Collectors.toList());
        validateHttpRequestStartLineSize(requestStartLine);

        return new HttpRequestStartLine(
                HttpMethod.of(requestStartLine.get(0)),
                requestStartLine.get(1),
                requestStartLine.get(2));
    }

    private static void validateHttpRequestStartLineSize(final List<String> requestStartLine) {
        if (requestStartLine.size() != 3) {
            throw new IllegalStateException();
        }
    }
}
