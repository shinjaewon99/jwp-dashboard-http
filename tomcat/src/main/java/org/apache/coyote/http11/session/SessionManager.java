package org.apache.coyote.http11.session;

import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Manager;

import java.io.IOException;

public class SessionManager implements Manager {

    private SessionManager() {
    }

    @Override
    public void add(final HttpSession session) {

    }

    @Override
    public HttpSession findSession(final String id) throws IOException {
        return null;
    }

    @Override
    public void remove(final HttpSession session) {

    }
}
