package org.apache.coyote.http11.request;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestBodyTest {

    @Test
    void HTTP_요청_바디_생성_테스트() throws IOException {
        // given
        final String httpRequest =
                "account=shin&password=123";

        // when
        HttpRequestBody httpRequestBody = HttpRequestBody.from(httpRequest);

        // then
        assertThat(httpRequestBody).isNotNull();
    }

    @Test
    void HTTP_요청_바디_VALUE_테스트() throws IOException {
        // given
        final String httpRequest =
                "account=shin&password=123";

        // when
        HttpRequestBody httpRequestBody = HttpRequestBody.from(httpRequest);

        // then
        assertThat(httpRequestBody.findBodyValue("account")).isEqualTo("shin");
        assertThat(httpRequestBody.findBodyValue("password")).isEqualTo("123");
    }
}
