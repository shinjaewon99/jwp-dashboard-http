package org.apache.coyote.http11.session;

import java.util.UUID;

public class JSessionIdGenerator {

    private JSessionIdGenerator() {
    }

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
