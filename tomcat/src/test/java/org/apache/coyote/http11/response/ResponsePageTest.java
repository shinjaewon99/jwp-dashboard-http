package org.apache.coyote.http11.response;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class ResponsePageTest {

    @Test
    void enum_URI_상수_값_테스트() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(ResponsePage.LOGIN_PAGE_URI.getHtmlUri()).isEqualTo("/login.html");
        softly.assertThat(ResponsePage.UNAUTHORIZED_PAGE_URI.getHtmlUri()).isEqualTo("/401.html");
        softly.assertThat(ResponsePage.INDEX_PAGE_URI.getHtmlUri()).isEqualTo("/index.html");
        softly.assertThat(ResponsePage.REGISTER_PAGE_URI.getHtmlUri()).isEqualTo("/register.html");

        // 모든 assert 결과를 확인
        softly.assertAll();
    }

    @Test
    void enum_상수_존재_테스트() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(ResponsePage.valueOf("LOGIN_PAGE_URI")).isEqualTo(ResponsePage.LOGIN_PAGE_URI);
        softly.assertThat(ResponsePage.valueOf("UNAUTHORIZED_PAGE_URI")).isEqualTo(ResponsePage.UNAUTHORIZED_PAGE_URI);
        softly.assertThat(ResponsePage.valueOf("INDEX_PAGE_URI")).isEqualTo(ResponsePage.INDEX_PAGE_URI);
        softly.assertThat(ResponsePage.valueOf("REGISTER_PAGE_URI")).isEqualTo(ResponsePage.REGISTER_PAGE_URI);

        // 모든 assert 결과를 확인
        softly.assertAll();
    }
}
