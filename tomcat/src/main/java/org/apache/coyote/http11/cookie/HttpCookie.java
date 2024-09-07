package org.apache.coyote.http11.cookie;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpCookie {

    private static final String SEPARATOR = "; ";
    private static final String DELIMITER = "=";

    private final Map<String, String> cookies = new HashMap<>();

    public HttpCookie(Map<String, String> cookies) {
        this.cookies.putAll(cookies);
    }

    private HttpCookie() {

    }

    public static HttpCookie empty() {
        return new HttpCookie();
    }

    public static HttpCookie from(final String cookie) {
        Map<String, String> cookiesStore = new HashMap<>();
        String[] lines = cookie.split(SEPARATOR);

        for (String line : lines) {
            String[] keyValue = line.split(DELIMITER);

            cookiesStore.put(keyValue[0], keyValue[1]);
        }

        return new HttpCookie(cookiesStore);
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
