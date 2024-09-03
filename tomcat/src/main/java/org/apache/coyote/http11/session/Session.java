package org.apache.coyote.http11.session;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private final Map<String, String> session = new HashMap<>();

    public void setSession(final String name, final String value) {
        session.put(name, value);
    }
}
