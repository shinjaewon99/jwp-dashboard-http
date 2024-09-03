package org.apache.coyote.http11.cookie;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {

    private static final String SEPARATOR = "; ";
    private static final String DELIMITER = "=";

    private final Map<String, String> cookies;

    public HttpCookie() {
        this.cookies = new HashMap<>();
    }

    public String findCookieValue(final String cookieKey) {
        return cookies.get(cookieKey);
    }

    public void put(final String key, final String value) {
        cookies.put(key, value);
    }

    public String getJSessionId() {
        return cookies.get("JSESSIONID");
    }
}
