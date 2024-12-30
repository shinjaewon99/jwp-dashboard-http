package org.apache.coyote.http11.request;

import org.apache.coyote.http11.cookie.HttpCookie;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

public class HttpRequestHeader {

    private Map<String, String> headers = new HashMap<>();

    public HttpRequestHeader(Map<String, String> headers) {
        this.headers = new HashMap<>(headers);
    }

    private HttpRequestHeader() {
    }

    public static HttpRequestHeader from(final String requestTarget) throws IOException {
        return Arrays.stream(requestTarget.split("\r\n"))
                .map(element -> element.split(": "))
                .collect(collectingAndThen(
                        toMap(element -> element[0], element -> element[1]), HttpRequestHeader::new));
    }

    public String findHeaderValue(final String headerKey) {
        return headers.get(headerKey);
    }

    public String findContentLength() {
        return headers.get("Content-Length");
    }

    public HttpCookie getCookie() {
        return HttpCookie.from(findHeaderValue("Cookie"));
    }
}
