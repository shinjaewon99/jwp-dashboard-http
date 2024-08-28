package org.apache.coyote.http11.response;

public class HttpResponse {

    private static final String HTML_CONTENT_TYPE = "text/html";
    private static final String CSS_CONTENT_TYPE = "text/css";
    private final String httpResponse;

    public HttpResponse(String httpResponse) {
        this.httpResponse = httpResponse;
    }


    public static HttpResponse of(final String requestTarget, final String responseBody) {
        return new HttpResponse(String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: " + loadContentType(requestTarget) + ";charset=utf-8 ",
                "Content-Length: " + responseBody.getBytes().length + " ",
                "",
                responseBody));
    }

    private static String loadContentType(final String requestTarget) {
        if (requestTarget.endsWith(".css")) {
            return CSS_CONTENT_TYPE;
        }
        return HTML_CONTENT_TYPE;
    }

    public String getHttpResponse() {
        return httpResponse;
    }
}
