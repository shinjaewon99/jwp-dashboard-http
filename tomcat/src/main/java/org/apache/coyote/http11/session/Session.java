package org.apache.coyote.http11.session;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private final String id;
    private final Map<String, String> session = new HashMap<>();

    public Session(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSession(final String name, final String value) {
        session.put(name, value);
    }

    public void clearSession() {
        session.clear();
    }
}
