package org.apache.coyote.http11.cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpCookieTest {

    private HttpCookie httpCookie;

    @BeforeEach
    public void setUp() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("account", "shin");
        cookies.put("JSESSIONID", "123456789");
        this.httpCookie = new HttpCookie(cookies);
    }

    @Test
    void HTTP_쿠키_생성_테스트() {

        // given
        final String cookies = "account=shin; JSESSIONID=123456789";

        // when
        HttpCookie resultCookie = HttpCookie.from(cookies);

        // then
        assertThat("shin").isEqualTo(resultCookie.findCookieValue("account"));
        assertThat("123456789").isEqualTo(resultCookie.findCookieValue("JSESSIONID"));
    }

    @Test
    void HTTP_쿠키_삽입_테스트() {

        // given, when
        httpCookie.put("password", "123");

        // then
        assertThat("123").isEqualTo(httpCookie.findCookieValue("password"));
    }

    @Test
    void HTTP_쿠키_JSESSIONID_불러오기_테스트() {
        assertThat("123456789").isEqualTo(httpCookie.getJSessionId());
    }

}