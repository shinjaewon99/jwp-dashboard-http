package org.apache.coyote.http11.request;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

class HttpRequestTest {

    @Test
    void HTTP_요청_생성_테스트() throws IOException {

        SoftAssertions softly = new SoftAssertions();

        // given
        final String httpRequest =
                "GET /index.html HTTP/1.1\r\n" +
                        "Host: example.com\r\n" +
                        "Content-Length: 26\r\n" +
                        "\r\n" +
                        "account=shin&password=123";

        // when
        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpRequest));
        HttpRequest request = HttpRequest.from(bufferedReader);

        // then
        HttpRequestStartLine startLine = request.getHttpRequestStartLine();

        softly.assertThat(startLine.getHttpMethod().name()).isEqualTo("GET");
        softly.assertThat(startLine.getRequestTarget()).isEqualTo("/index.html");
        softly.assertThat(startLine.getHttpVersion()).isEqualTo("HTTP/1.1");

        HttpRequestHeader requestHeader = request.getHttpRequestHeader();

        softly.assertThat(requestHeader.findHeaderValue("Host")).isEqualTo("example.com");
        softly.assertThat(requestHeader.findContentLength()).isEqualTo("26");

        HttpRequestBody requestBody = request.getHttpRequestBody();
        softly.assertThat(requestBody.findBodyValue("account")).isEqualTo("shin");
        softly.assertThat(requestBody.findBodyValue("password").trim()).isEqualTo("123");
    }
}
