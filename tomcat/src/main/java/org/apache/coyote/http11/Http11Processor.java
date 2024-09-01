package org.apache.coyote.http11;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.model.User;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.*;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseEntity;
import org.apache.coyote.http11.response.HttpStatus;
import org.apache.coyote.http11.response.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
    private static final String QUERY_STRING_ACCOUNT = "account";
    private static final String QUERY_STRING_PASSWORD = "password";
    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // HTTP 요청의 첫번째 라인
            final HttpRequest requestStartLine = HttpRequest.from(bufferedReader);
            final HttpRequestStartLine httpRequestStartLine = requestStartLine.getHttpRequestStartLine();
            final HttpRequestHeader httpRequestHeader = requestStartLine.getHttpRequestHeader();
            final HttpRequestBody httpRequestBody = requestStartLine.getHttpRequestBody();

            final HttpResponseEntity responseEntity = createHttpResponse(httpRequestStartLine, httpRequestHeader, httpRequestBody);
            final String response = HttpResponse.from(responseEntity).getFormatHttpResponse();

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private HttpResponseEntity createHttpResponse(final HttpRequestStartLine httpRequestStartLine, final HttpRequestHeader httpRequestHeader,
                                                  final HttpRequestBody httpRequestBody) throws IOException {

        final String requestTarget = httpRequestStartLine.getRequestTarget();

        // "/" 루트 경로일때
        if (requestTarget.equals("/")) {
            final var responseBody = "Hello world!";

            return HttpResponseEntity
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .requestTarget(requestTarget)
                    .responseBody(responseBody)
                    .build();
        }

        // /login 경로 일때
        if (requestTarget.equals("/login")) {
            return createLogin(httpRequestStartLine, httpRequestHeader, httpRequestBody);
        }

        final String responseBody = createUrlResource(requestTarget);

        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.OK)
                .requestTarget(requestTarget)
                .responseBody(responseBody)
                .build();
    }

    private HttpResponseEntity createLogin(final HttpRequestStartLine httpRequestStartLine,
                                           final HttpRequestHeader httpRequestHeader, final HttpRequestBody httpRequestBody) throws IOException {
        HttpMethod httpMethod = httpRequestStartLine.getHttpMethod();
        String requestTarget = httpRequestStartLine.getRequestTarget();

        if (httpMethod == HttpMethod.GET) {
            return HttpResponseEntity
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .requestTarget(requestTarget)
                    .responsePage(ResponsePage.LOGIN_PAGE_URI)
                    .build();
        }

        final Map<String, String> parseQueryString = parseQueryString(requestTarget);
        final User user = findAccount(parseQueryString);

        boolean checkedPassword = user.checkPassword(parseQueryString.get(QUERY_STRING_PASSWORD));

        // 비밀번호가 불일치 할경우
        if (!checkedPassword) {
            log.info("PASSWORD 불일치" + user.getAccount());
            return HttpResponseEntity
                    .builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .requestTarget(requestTarget)
                    .responsePage(ResponsePage.UNAUTHORIZED_PAGE_URI)
                    .build();
        }

        final String responseBody = createUrlResource(requestTarget);
        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.FOUND)
                .requestTarget(requestTarget)
                .responsePage(ResponsePage.INDEX_PAGE_URI)
                .responseBody(responseBody)
                .build();
    }

    private User findAccount(Map<String, String> parseQueryString) {
        String findAccount = parseQueryString.get(QUERY_STRING_ACCOUNT);
        return InMemoryUserRepository.findByAccount(findAccount).orElseThrow(NoSuchElementException::new);
    }

    private Map<String, String> parseQueryString(final String requestTarget) {
        // /search?q=java&sort=ascending
        // q가 key, java가 value
        final Map<String, String> queryParams = new HashMap<>();
        final int questionMarkIndex = requestTarget.indexOf("?");

        if (questionMarkIndex == -1) {
            return queryParams;
        }

        final String subQueryString = requestTarget.substring(questionMarkIndex + 1);

        // q=java sort=ascending
        final String[] split = subQueryString.split("&");

        for (String s : split) {
            // q java
            // sort ascending
            final String[] split1 = s.split("=");

            if (split1.length == 2) {
                queryParams.put(split1[0], split1[1]);
            } else if (split1.length == 1) {
                // 값이 없는 경우에는 빈 문자열을 값으로 설정합니다.
                queryParams.put(split1[0], "");
            }
        }
        return queryParams;
    }

    private String createUrlResource(String requestTarget) throws IOException {
        // 루트 경로가 아닐경우
        final URL resource = getClass()
                .getClassLoader()
                .getResource("static" + requestTarget);

        if (resource == null) {
            // 리소스를 찾지 못한 경우에 대한 처리
            throw new FileNotFoundException("Resource not found: " + requestTarget);
        }

        final String filePath = resource.getFile();
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
