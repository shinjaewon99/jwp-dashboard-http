package org.apache.coyote.http11.response;

import org.apache.coyote.http11.request.HttpRequestHeader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

public class HttpResponseHeader {

    private Map<String, String> headers = new HashMap<>();

    public HttpResponseHeader(Map<String, String> headers) {
        this.headers = new HashMap<>(headers);
    }

    private HttpResponseHeader() {
    }

    public static HttpResponseHeader from(final String requestTarget) throws IOException {
        return Arrays.stream(requestTarget.split("\r\n"))
                .map(element -> element.split(": "))
                .collect(collectingAndThen(
                        toMap(element -> element[0], element -> element[1]), HttpResponseHeader::new));
    }
}
