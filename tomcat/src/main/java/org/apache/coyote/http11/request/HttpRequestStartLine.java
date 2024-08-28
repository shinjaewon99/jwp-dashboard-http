package org.apache.coyote.http11.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    public static HttpRequestStartLine parseHttpRequestStartLine(final String httpRequest) throws IOException {
        String[] requestStartLine = httpRequest.split(BLANK);
        validateHttpRequestStartLineSize(requestStartLine);

        return new HttpRequestStartLine(
                HttpMethod.of(requestStartLine[0]),
                requestStartLine[1],
                requestStartLine[2]);
    }

    private static void validateHttpRequestStartLineSize(String[] requestStartLine) {
        if (requestStartLine.length != 3) {
            throw new IllegalStateException();
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
