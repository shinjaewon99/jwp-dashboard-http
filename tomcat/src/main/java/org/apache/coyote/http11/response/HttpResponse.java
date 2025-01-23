package org.apache.coyote.http11.response;

import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.apache.coyote.http11.common.HttpVersion.HTTP1_1;

@Getter
public class HttpResponse {

    private static final String CRLF = "\r\n";
    private static final String BLANK_LINE = "";

    private String response;
    private HttpResponseStatusStart httpResponseStatusStart;
    private HttpResponseHeader httpResponseHeader;
    private HttpResponseBody httpResponseBody;

    public HttpResponse(final String response) {
        this.response = response;
    }

    @Builder
    public HttpResponse(final HttpResponseStatusStart httpResponseStatusStart, final HttpResponseHeader httpResponseHeader, final HttpResponseBody httpResponseBody) {
        this.httpResponseStatusStart = httpResponseStatusStart;
        this.httpResponseHeader = httpResponseHeader;
        this.httpResponseBody = httpResponseBody;
    }

    public static HttpResponse from(final HttpResponseEntity httpResponse) throws IOException {
        HttpResponseBody responseBody = generateResponseBody(httpResponse);
        HttpResponseHeader headers = generateResponseHeaders(httpResponse, responseBody);
        final HttpStatus httpStatus = httpResponse.getHttpStatus();

        return HttpResponse
                .builder()
                .httpResponseStatusStart(HttpResponseStatusStart.of(HTTP1_1, httpStatus))
                .httpResponseHeader(headers)
                .httpResponseBody(responseBody)
                .build();
    }

    private static HttpResponseBody generateResponseBody(final HttpResponseEntity httpResponse) throws IOException {
        HttpResponseBody responseBody = httpResponse.getResponseBody();
        String location = httpResponse.getLocation();

        // Http 응답중 body가 비어있는경우
        if (responseBody == null) {
            responseBody = generateResponseBody(location);
        }
        return responseBody;
    }

    private static HttpResponseHeader generateResponseHeaders(final HttpResponseEntity httpResponse, final HttpResponseBody responseBody) throws IOException {
        HttpStatus httpStatus = httpResponse.getHttpStatus();
        final String location = httpResponse.getLocation();

        if (httpStatus == HttpStatus.FOUND) {
            return new HttpResponseHeader()
                    .location(location)
                    .setCookie(httpResponse.getHttpCookie());
        }

        return new HttpResponseHeader()
                .contentType(httpResponse.getContentType())
                .contentTypeLength(responseBody);
    }

    public String getHttpResponse() {
        return null;
    }

    private String formatResponseStatusLine(final HttpResponseStatusStart httpResponseStatusStart) {
        return String.format("%s %s %s",
                httpResponseStatusStart.getHttpVersion().getVersion(),
                httpResponseStatusStart.getHttpStatus().getHttpStatusCode(),
                httpResponseStatusStart.getHttpStatus().name());
    }

    private String formatResponseHeader(final HttpResponseHeader header) {
        return null;
    }

    private String formatResponseBody() {
        return httpResponseBody.getBody();
    }

    private static HttpResponseBody generateResponseBody(final String htmlUri) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("static" + htmlUri);
        return HttpResponseBody.from(new String(Files.readAllBytes(new File(resource.getFile()).toPath())));
    }
}
