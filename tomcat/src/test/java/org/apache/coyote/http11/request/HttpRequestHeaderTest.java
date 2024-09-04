package org.apache.coyote.http11.request;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestHeaderTest {

    @Test
    void HTTP_요청_헤더_생성_테스트() throws IOException {

        // given
        final String httpRequest =
                "Host: example.com\r\n" +
                        "Content-Length: 21\r\n" +
                        "\r\n";

        // when
        HttpRequestHeader request = HttpRequestHeader.from(httpRequest);

        // then
        assertThat("example.com").isEqualTo(request.findHeaderValue("Host"));
        assertThat("example.com").isEqualTo(request.findHeaderValue("Host"));
    }

    @Test
    void HTTP_요청_헤더_CONTETLENGTH_길이_테스트() throws IOException {

        // given
        final String httpRequest =
                "Host: example.com\r\n" +
                        "Content-Length: 21\r\n" +
                        "\r\n";

        // when
        HttpRequestHeader request = HttpRequestHeader.from(httpRequest);

        // then
        assertThat("21").isEqualTo(request.findContentLength());
    }
}
