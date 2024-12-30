package org.apache.coyote.http11.response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

public class HttpResponseBody {
    private Map<String, String> body = new HashMap<>();

    public HttpResponseBody(final Map<String, String> body) {
        this.body = new HashMap<>(body);
    }

    private HttpResponseBody() {
    }

    public static HttpResponseBody from(final String body) {
        if (body.isEmpty()) {
            return HttpResponseBody.empty();
        }

        // body 필드 구분
        return Arrays.stream(body.split("&"))
                .map(element -> element.split("="))
                .collect(collectingAndThen
                        (toMap(element -> element[0], element -> element[1]),
                                HttpResponseBody::new));
    }

    private static HttpResponseBody empty() {
        return new HttpResponseBody();
    }
}
