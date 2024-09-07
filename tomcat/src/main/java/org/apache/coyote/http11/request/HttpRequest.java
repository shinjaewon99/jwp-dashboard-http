package org.apache.coyote.http11.request;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;

@Getter
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
        HttpRequestBody requestBody = parseHttpRequestBody(requestHeader.findContentLength(), bufferedReader);

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
        StringBuilder httpRequestHeaderBuilder = new StringBuilder();
        String requestTarget = bufferedReader.readLine();

        while (!requestTarget.isEmpty()) {
            httpRequestHeaderBuilder.append(requestTarget).append("\r\n");
            requestTarget = bufferedReader.readLine();
        }

        // Http 헤더와 Body 사이의 BLANK 라인 추가
        httpRequestHeaderBuilder.append(requestTarget).append("\r\n");

        return HttpRequestHeader.from(httpRequestHeaderBuilder.toString());
    }

    private static HttpRequestBody parseHttpRequestBody(final String contentLength,
                                                        final BufferedReader bufferedReader) throws IOException {
        if(contentLength == null) {
            return HttpRequestBody.httpRequestBodyNone();
        }

        int findContentLength = Integer.parseInt(contentLength);
        char[] requestHttpBody = new char[findContentLength];

        // 0부터 content-length까지 한글자씩 잘라서 문자배열에 담아준다.
        bufferedReader.read(requestHttpBody, 0, findContentLength);
        return HttpRequestBody.from(new String(requestHttpBody));
    }
}
