package org.apache.coyote.http11.response;

import org.apache.coyote.http11.cookie.HttpCookie;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class HttpResponseEntityTest {

    private HttpResponseEntity entity;

    @BeforeEach
    void setUp() {
        Map<String, String> cookie = Map.of("JSESSIONID", "1234567");
        HttpCookie httpCookie = new HttpCookie(cookie);
        entity = HttpResponseEntity.builder()
                .httpStatus(HttpStatus.OK)
                .requestTarget("/index.html")
                .responseBody("account=shin&password=123")
                .responsePage(ResponsePage.INDEX_PAGE_URI)
                .httpCookie(httpCookie)
                .build();
    }

    @Test
    void HTTP_엔티티_응답_생성_테스트() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(entity.getHttpStatus()).isEqualTo(HttpStatus.OK);
        softly.assertThat(entity.getRequestTarget()).isEqualTo("/index.html");
        softly.assertThat(entity.getResponseBody()).isEqualTo("account=shin&password=123");
        softly.assertThat(entity.getResponsePage()).isEqualTo(ResponsePage.INDEX_PAGE_URI);
    }

    @Test
    void HTTP_쿠키_생성_테스트() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(entity.getHttpCookie().findCookieValue("JSESSIONID")).isEqualTo("1234567");
        softly.assertThat(entity.getHttpCookie().getJSessionId()).isEqualTo("1234567");
        softly.assertThat(entity.getHttpCookie()).isNotNull();
    }
}
