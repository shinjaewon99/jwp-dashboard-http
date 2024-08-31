package org.apache.coyote.http11.response;

import lombok.Getter;

@Getter
public class HttpResponse {

    private static final String HTML_CONTENT_TYPE = "text/html";
    private static final String CSS_CONTENT_TYPE = "text/css";
    private final String httpResponse;

    public HttpResponse(String httpResponse) {
        this.httpResponse = httpResponse;
    }


    public static HttpResponse of(final String statusCode, final String requestTarget, final String responseBody) {
        return new HttpResponse(String.join("\r\n",
                "HTTP/1.1 " + statusCode + " ",
                "Content-Type: " + loadContentType(requestTarget) + ";charset=utf-8 ",
                "Content-Length: " + responseBody.getBytes().length + " ",
                "",
                responseBody));
    }

    public static HttpResponse from(final HttpResponseEntity httpResponse) {
        final HttpStatus httpStatus = httpResponse.getHttpStatus();
        final String fullHttpStatus = httpStatus.getHttpStatusCode() + " " + httpStatus.name();
        final String requestTarget = httpResponse.getRequestTarget();
        final String responseBody = httpResponse.getResponseBody();

        return HttpResponse.of(fullHttpStatus, requestTarget, responseBody);
    }

    private static String loadContentType(final String requestTarget) {
        if (requestTarget.endsWith(".css")) {
            return CSS_CONTENT_TYPE;
        }
        return HTML_CONTENT_TYPE;
    }
}
