package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

    private final HttpRequestStartLine httpRequestStartLine;
    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;

    public HttpRequest(final HttpRequestStartLine httpRequestStartLine,
                       final HttpRequestHeader httpRequestHeader,
                       final HttpRequestBody httpRequestBody) {
        this.httpRequestStartLine = httpRequestStartLine;
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public static HttpRequest from(final BufferedReader bufferedReader) throws IOException {
        HttpRequestStartLine requestStartLine = parseHttpRequestStartLine(bufferedReader);
        HttpRequestHeader requestHeader = parseHttpRequestHeader(bufferedReader);
        HttpRequestBody requestBody = parseHttpRequestBody(bufferedReader);

        return new HttpRequest(requestStartLine, requestHeader, requestBody);
    }

    private static HttpRequestStartLine parseHttpRequestStartLine(final BufferedReader bufferedReader) throws IOException {
        String requestTarget = bufferedReader.readLine();

        if (requestTarget == null) {
            throw new IllegalArgumentException();
        }

        return HttpRequestStartLine.from(requestTarget);
    }

    private static HttpRequestHeader parseHttpRequestHeader(final BufferedReader bufferedReader) throws IOException {
        return HttpRequestHeader.from(bufferedReader);
    }

    private static HttpRequestBody parseHttpRequestBody(final BufferedReader bufferedReader) throws IOException {
        String requestTarget = bufferedReader.readLine();


        return HttpRequestBody.from(requestTarget);
    }


    public HttpRequestStartLine getHttpRequestStartLine() {
        return httpRequestStartLine;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

    public HttpRequestBody getHttpRequestBody() {
        return httpRequestBody;
    }
}
