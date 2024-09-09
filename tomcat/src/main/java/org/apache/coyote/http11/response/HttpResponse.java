package org.apache.coyote.http11.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.http11.cookie.HttpCookie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@Getter
@RequiredArgsConstructor
public class HttpResponse {

    private static final String CRLF = "\r\n";
    private static final String BLANK_LINE = "";

    private final String formatHttpResponse;

    public static HttpResponse from(final HttpResponseEntity httpResponse) throws IOException {

        final String htmlUri = httpResponse.getResponsePage().getHtmlUri();
        final String requestTarget = httpResponse.getRequestTarget();
        final HttpStatus httpStatus = httpResponse.getHttpStatus();
        String responseBody = httpResponse.getResponseBody();

        // Http 응답중 body가 비어있는경우
        if (responseBody == null) {
            responseBody = loadResponseBody(htmlUri);
        }

        if (httpStatus == HttpStatus.FOUND) {
            return new HttpResponse(String.join(
                    CRLF,
                    generateHttpStatus(httpStatus),
                    generateLocation(httpResponse),
                    generateCookie(httpResponse)
            ));
        }

        return new HttpResponse(String.join(
                CRLF,
                generateHttpStatus(httpStatus),
                generateContentType(requestTarget),
                generateContentLength(responseBody),
                BLANK_LINE,
                responseBody));
    }

    private static String loadResponseBody(final String htmlUri) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("static" + htmlUri);
        File file = new File(resource.getFile());
        return new String(Files.readAllBytes(file.toPath()));
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

    private static String generateContentLength(final String responseBody) {
        return String.format("Content-Length: %s ", responseBody.getBytes().length);
    }

    private static String generateLocation(final HttpResponseEntity httpResponse) {
        final String htmlUri = httpResponse.getResponsePage().getHtmlUri();

        if (htmlUri == null) {
            return "";
        }

        return String.format("Location: %s", htmlUri);
    }


    private static String generateCookie(final HttpResponseEntity httpResponse) {
        final HttpCookie httpCookie = httpResponse.getHttpCookie();

        if (httpCookie == null) {
            return "";
        }

        final String jSessionId = httpCookie.getJSessionId();

        if (jSessionId == null) {
            return "";
        }

        return String.format("Set-Cookie: JSESSIONID=%s", jSessionId);
    }
}
