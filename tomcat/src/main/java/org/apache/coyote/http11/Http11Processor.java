package org.apache.coyote.http11;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.model.User;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.*;
import org.apache.coyote.http11.response.*;
import org.apache.coyote.http11.session.JSessionIdGenerator;
import org.apache.coyote.http11.session.Session;
import org.apache.coyote.http11.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.apache.coyote.http11.response.ResponsePage.*;


public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
    private static final String ACCOUNT_FIELD = "account";
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAIL_FIELD = "email";
    private static final String CSS = ".css";
    private final SessionManager sessionManager = new SessionManager();
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

        final String requestTarget = httpRequestStartLine.getPath();

        // "/" 루트 경로일때
        if (requestTarget.equals("/")) {
            return createMainPage(requestTarget);
        }

        // /login 경로 일때
        if (requestTarget.startsWith("/login")) {
            return createLogin(httpRequestStartLine, httpRequestBody);
        }

        // register 경로 일때
        if (requestTarget.startsWith("/register")) {
            return createRegister(httpRequestStartLine, httpRequestBody);
        }

        return findStaticResource(requestTarget);
    }

    private HttpResponseEntity createMainPage(final String requestTarget) {
        final var responseBody = "Hello world!";

        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.OK)
                .contentType(generateContentType(requestTarget))
                .responseBody(HttpResponseBody.from(responseBody))
                .responsePage(EMPTY)
                .build();
    }

    private HttpResponseEntity createLogin(final HttpRequestStartLine httpRequestStartLine, final HttpRequestBody httpRequestBody) throws IOException {
        final HttpMethod httpMethod = httpRequestStartLine.getHttpMethod();
        final String requestTarget = httpRequestStartLine.getPath();
        final String account = httpRequestBody.findBodyValue(ACCOUNT_FIELD);

        if (httpMethod == HttpMethod.GET && account == null) {
            return HttpResponseEntity
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .contentType(generateContentType(requestTarget))
                    .responsePage(LOGIN_PAGE_URI)
                    .build();
        }

        final User user = findAccount(account);
        final String password = httpRequestBody.findBodyValue(PASSWORD_FIELD);

        boolean checkedPassword = user.checkPassword(password);

        // 비밀번호가 불일치 할경우
        if (!checkedPassword) {
            return createLoginFail(requestTarget, user);
        }

        return createLoginSuccess(requestTarget, user);
    }

    private HttpResponseEntity createLoginFail(final String requestTarget, final User user) {
        log.info("PASSWORD 불일치" + user.getAccount());

        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .contentType(generateContentType(requestTarget))
                .responsePage(UNAUTHORIZED_PAGE_URI)
                .build();
    }

    private HttpResponseEntity createLoginSuccess(final String requestTarget, final User user) {
        log.info("account {} 로그인 성공", user.getAccount());

        HttpResponseEntity httpResponseEntity = HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.FOUND)
                .contentType(generateContentType(requestTarget))
                .responsePage(INDEX_PAGE_URI)
                .build();

        final String jSessionId = JSessionIdGenerator.generateSessionId();
        httpResponseEntity.setCookie("JSESSIONID", jSessionId);

        // Session에 쿠키 담기
        Session session = new Session(jSessionId);
        sessionManager.add(session);

        return httpResponseEntity;
    }


    private HttpResponseEntity findStaticResource(final String requestTarget) throws IOException {

        // 루트 경로가 아닐경우
        final URL resource = getClass()
                .getClassLoader()
                .getResource("static" + requestTarget);

        if (resource == null) {
            // 리소스를 찾지 못한 경우에 대한 처리
            throw new FileNotFoundException("Resource not found: " + requestTarget);
        }

        final File file = new File(resource.getFile());
        final String responseBody = new String(Files.readAllBytes(file.toPath()));

        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.OK)
                .contentType(generateContentType(requestTarget))
                .responseBody(HttpResponseBody.from(responseBody))
                .build();
    }

    private HttpResponseEntity createRegister(final HttpRequestStartLine httpRequestStartLine, final HttpRequestBody httpRequestBody) {
        final HttpMethod httpMethod = httpRequestStartLine.getHttpMethod();
        final String requestTarget = httpRequestStartLine.getPath();

        if (httpMethod == HttpMethod.GET) {
            return HttpResponseEntity
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .contentType(generateContentType(requestTarget))
                    .responsePage(REGISTER_PAGE_URI)
                    .build();
        }

        final String account = httpRequestBody.findBodyValue(ACCOUNT_FIELD);
        final String password = httpRequestBody.findBodyValue(PASSWORD_FIELD);
        final String email = httpRequestBody.findBodyValue(EMAIL_FIELD);

        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);

        log.info("{} user 회원가입 성공", account);

        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.FOUND)
                .contentType(generateContentType(requestTarget))
                .responsePage(INDEX_PAGE_URI)
                .build();
    }

    private ContentType generateContentType(final String requestTarget) {
        if (requestTarget.endsWith(CSS)) {
            return ContentType.CSS;
        }
        return ContentType.HTML;
    }

    private User findAccount(final String account) {
        return InMemoryUserRepository.findByAccount(account).orElseThrow(NoSuchElementException::new);
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
