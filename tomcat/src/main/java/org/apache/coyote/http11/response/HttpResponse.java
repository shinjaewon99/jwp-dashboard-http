package org.apache.coyote.http11.response;

import lombok.Getter;
import org.apache.coyote.http11.cookie.HttpCookie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

@Getter
public class HttpResponse {

    private static final String CRLF = "\r\n";
    private static final String BLANK_LINE = "";

    private String response;
    private HttpResponseStartLine httpResponseStartLine;
    private HttpResponseHeader httpResponseHeader;
    private HttpResponseBody httpResponseBody;

    public HttpResponse(final String response) {
        this.response = response;
    }

    public HttpResponse(final HttpResponseStartLine httpResponseStartLine, final HttpResponseHeader httpResponseHeader, final HttpResponseBody httpResponseBody) {
        this.httpResponseStartLine = httpResponseStartLine;
        this.httpResponseHeader = httpResponseHeader;
        this.httpResponseBody = httpResponseBody;
    }

    public static HttpResponse from(final HttpResponseEntity httpResponse) throws IOException {

        final String htmlUri = httpResponse.getResponsePage().getHtmlUri();
        final HttpStatus httpStatus = httpResponse.getHttpStatus();
        HttpResponseBody responseBody = httpResponse.getResponseBody();

        // Http 응답중 body가 비어있는경우
        if (responseBody == null) {
            responseBody = generateResponseBody(htmlUri);
        }

        if (httpStatus == HttpStatus.FOUND) {
            return new HttpResponse(String.join(
                    CRLF,
                    generateHttpStatus(httpStatus),
                    generateLocation(httpResponse.getLocation()),
                    generateCookie(httpResponse.getHttpCookie())
            ));
        }

        return new HttpResponse(String.join(
                CRLF,
                generateHttpStatus(httpStatus),
                generateContentType(httpResponse.getContentType().getName()),
                generateContentLength(responseBody),
                BLANK_LINE,
                responseBody.getBody()));
    }

    private static HttpResponseBody generateResponseBody(final String htmlUri) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("static" + htmlUri);
        return HttpResponseBody.from(new String(Files.readAllBytes(new File(resource.getFile()).toPath())));
    }

    private static String generateHttpStatus(final HttpStatus httpStatus) {
        return String.format("HTTP/1.1 %s %s ", httpStatus.getHttpStatusCode(), httpStatus.name());
    }

    private static String generateContentType(final String requestTarget) {
        if (requestTarget.equals(".css")) {
            return "Content-Type: text/css;charset=utf-8 ";
        }
        return "Content-Type: text/html;charset=utf-8 ";
    }

    private static String generateContentLength(final HttpResponseBody responseBody) {
        final String body = responseBody.getBody();
        return String.format("Content-Length: %s ", body.getBytes().length);
    }

    private static String generateLocation(final String location) {
        return Optional.ofNullable(location)
                .map(destination -> String.format("Location: %s", destination))
                .orElse("");
    }


    private static String generateCookie(final HttpCookie httpCookie) {
        return Optional.ofNullable(httpCookie)
                .map(cookie -> cookie.getJSessionId())
                .map(id -> String.format("Set-Cookie: JSESSIONID=%s", id))
                .orElse("");
    }
}
