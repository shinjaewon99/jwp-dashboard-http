package org.apache.coyote.http11.response;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class HttpStatusTest {

    @Test
    void enum_상태_상수_값_테스트() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(HttpStatus.OK.getHttpStatusCode()).isEqualTo("200");
        softly.assertThat(HttpStatus.CREATED.getHttpStatusCode()).isEqualTo("201");
        softly.assertThat(HttpStatus.FOUND.getHttpStatusCode()).isEqualTo("302");
        softly.assertThat(HttpStatus.UNAUTHORIZED.getHttpStatusCode()).isEqualTo("401");

        // 모든 assert 결과를 확인
        softly.assertAll();
    }

    @Test
    void enum_상수_존재_테스트() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(HttpStatus.valueOf("OK")).isEqualTo(HttpStatus.OK);
        softly.assertThat(HttpStatus.valueOf("CREATED")).isEqualTo(HttpStatus.CREATED);
        softly.assertThat(HttpStatus.valueOf("FOUND")).isEqualTo(HttpStatus.FOUND);
        softly.assertThat(HttpStatus.valueOf("UNAUTHORIZED")).isEqualTo(HttpStatus.UNAUTHORIZED);

        // 모든 assert 결과를 확인
        softly.assertAll();
    }
}
