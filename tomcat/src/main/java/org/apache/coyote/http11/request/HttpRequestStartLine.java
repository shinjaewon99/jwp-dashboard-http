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
    private static final String BLANK = " ";
    private static final int HTTP_METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int HTTP_VERSION = 2;
    private static final int REQUEST_START_LINE_LENGTH = 3;

    private final HttpMethod httpMethod;
    private final String path;
    private final String httpVersion;

    public HttpRequestStartLine(HttpMethod httpMethod, String path, String httpVersion) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestStartLine from(final String httpRequest) throws IOException {
        List<String> requestStartLine = Arrays.stream(httpRequest.split(BLANK)).collect(Collectors.toList());
        validateHttpRequestStartLineSize(requestStartLine);

        return new HttpRequestStartLine(
                HttpMethod.of(requestStartLine.get(HTTP_METHOD_INDEX)),
                requestStartLine.get(PATH_INDEX),
                requestStartLine.get(HTTP_VERSION));
    }

    private static void validateHttpRequestStartLineSize(final List<String> requestStartLine) {
        if (requestStartLine.size() != REQUEST_START_LINE_LENGTH) {
            throw new IllegalStateException();
        }
    }
}
