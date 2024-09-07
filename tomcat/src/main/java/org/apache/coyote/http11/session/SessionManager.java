package org.apache.coyote.http11.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static final Map<String, Session> SESSION_STORE = new HashMap<>();

    public void add(final Session session) {
        SESSION_STORE.put(session.getId(), session);
    }

    public Session findSession(final String id) {
        return SESSION_STORE.get(id);
    }

    public void remove(final Session session) {
        SESSION_STORE.remove(session.getId());
    }
}
