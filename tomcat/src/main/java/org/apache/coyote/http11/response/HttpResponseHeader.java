package org.apache.coyote.http11.response;

import lombok.Getter;
import org.apache.coyote.http11.cookie.HttpCookie;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class HttpResponseHeader {

    private Map<String, String> headers;

    public HttpResponseHeader() {
        this(new LinkedHashMap<>());
    }

    private HttpResponseHeader(final Map<String, String> headers) {
        this.headers = new LinkedHashMap<>(headers);
    }

    public HttpResponseHeader location(final String location) {
        return Optional.ofNullable(location)
                .map(destination -> {
                    headers.put("Location: ", destination);
                    return this;
                })
                .orElse(this);
    }

    public HttpResponseHeader setCookie(final HttpCookie httpCookie) {
        return Optional.ofNullable(httpCookie)
                .map(cookie -> cookie.getJSessionId())
                .map(id -> {
                    headers.put("Set-Cookie: JSESSIONID=%s", id);
                    return this;
                })
                .orElse(this);
    }

    public HttpResponseHeader contentType(final ContentType contentType) {
        setHeader("Content-Type", contentType.getName() + ";charset=utf-8");
        return this;
    }

    public HttpResponseHeader contentTypeLength(final HttpResponseBody responseBody) {
        final String body = responseBody.getBody();
        setHeader("Content-Length:", String.valueOf(body.getBytes().length));
        return this;
    }

    private void setHeader(final String key, final String value) {
        headers.put(key, value);
    }
}
