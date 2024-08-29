package org.apache.coyote.http11.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestBodyTest {

    @Test
    @DisplayName("HTTP 요청 바디에서 key-value 쌍을 올바르게 파싱하는지 검증")
    void createHttpRequestBody() throws IOException {
        // given
        String requestBodyString = "account=shin&password=1234@123";

        // when
        HttpRequestBody httpRequestBody = HttpRequestBody.from(requestBodyString);

        // then
        assertThat(httpRequestBody).isNotNull();
        assertThat(httpRequestBody.findBodyValue("account")).isEqualTo("shin");
        assertThat(httpRequestBody.findBodyValue("password")).isEqualTo("1234@123");
    }
}