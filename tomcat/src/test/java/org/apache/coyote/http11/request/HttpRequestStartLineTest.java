package org.apache.coyote.http11.request;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestStartLineTest {

    @Test
    void HTTP_요청_시작_라인_테스트() throws IOException {

        // given
        final String httpRequest =
                "GET /index.html HTTP/1.1\r\n";

        // when
        HttpRequestStartLine request = HttpRequestStartLine.from(httpRequest);

        // then
        assertThat("GET").isEqualTo(request.getHttpMethod().name());
        assertThat("/index.html").isEqualTo(request.getRequestTarget());
        assertThat("HTTP/1.1\r\n").isEqualTo(request.getHttpVersion());
    }

    @Test
    void HTTP_요청_시작_라인_포맷_예외_테스트() {

        // given
        final String httpRequest = "GET /index.html HTTP/1.1 SHIN/1.2\r\n";

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            HttpRequestStartLine.from(httpRequest);
        });
    }
}