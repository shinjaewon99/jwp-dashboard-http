package org.apache.coyote.http11.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HttpMethodTest {


    @Test
    void HTTP_메소드가_GET인경우() {
        // given
        String requestHttpMethod = "GET";

        // when
        HttpMethod httpMethod = HttpMethod.of(requestHttpMethod);

        // then
        Assertions.assertThat(httpMethod.name()).isEqualTo(requestHttpMethod);
    }

    @Test
    void HTTP_메소드가_POST인경우() {
        // given
        String requestHttpMethod = "POST";

        // when
        HttpMethod httpMethod = HttpMethod.of(requestHttpMethod);

        // then
        Assertions.assertThat(httpMethod.name()).isEqualTo(requestHttpMethod);
    }

    @Test
    void 유효하지_않은_HTTP_메소드인경우() {
        // given
        String requestHttpMethod = "METHOD";

        // when & then
        Assertions.assertThatThrownBy(() -> HttpMethod.of(requestHttpMethod))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
