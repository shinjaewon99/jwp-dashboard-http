package org.apache.coyote.http11.request;

import org.apache.coyote.http11.cookie.HttpCookie;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {

    private Map<String, String> headers = new HashMap<>();

    public HttpRequestHeader(Map<String, String> headers) {
        this.headers = new HashMap<>(headers);
    }

    private HttpRequestHeader() {
    }

    public static HttpRequestHeader from(final String requestTarget) throws IOException {
        Map<String, String> headerMap = new HashMap<>();

        String[] lines = requestTarget.split("\r\n");
        for (String line : lines) {
            String[] split = line.split(": ");
            headerMap.put(split[0], split[1]);
        }

        return new HttpRequestHeader(headerMap);
    }

    public String findHeaderValue(final String headerKey) {
        return headers.get(headerKey);
    }

    public String findContentLength() {
        return headers.get("Content-Length");
    }

    public HttpCookie getCookie() {
        return HttpCookie.from(headers.get("Cookie"));
    }
}
