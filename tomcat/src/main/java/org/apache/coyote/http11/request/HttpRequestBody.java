package org.apache.coyote.http11.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestBody {

    private Map<String, String> body = new HashMap<>();

    // 전송하는 데이터가 없는경우 body는 비어있음
    private HttpRequestBody() {
    }

    public static HttpRequestBody httpRequestBodyNone() {
        return new HttpRequestBody();
    }

    private HttpRequestBody(Map<String, String> body) {
        this.body = new HashMap<>(body);
    }

    public static HttpRequestBody from(final String httpRequest) throws IOException {
        Map<String, String> bodyMap = new HashMap<>();

        if (httpRequest.isEmpty()) {
            return httpRequestBodyNone();
        }

        String[] httpRequestBody = httpRequest.split("&");
        for (String requestBody : httpRequestBody) {
            String[] body = requestBody.split("=");
            bodyMap.put(body[0], body[1]);
        }

        return new HttpRequestBody(bodyMap);
    }

    public String findBodyValue(final String bodyKey) {
        return body.get(bodyKey);
    }
}
