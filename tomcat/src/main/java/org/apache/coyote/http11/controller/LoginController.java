package org.apache.coyote.http11.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.cookie.HttpCookie;
import org.apache.coyote.http11.request.*;
import org.apache.coyote.http11.response.HttpResponseEntity;
import org.apache.coyote.http11.response.HttpStatus;
import org.apache.coyote.http11.session.JSessionIdGenerator;
import org.apache.coyote.http11.session.Session;
import org.apache.coyote.http11.session.SessionManager;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.apache.coyote.http11.response.ResponsePage.*;

public class LoginController implements Controller {
    private static final String ACCOUNT_FIELD = "account";
    private static final String PASSWORD_FIELD = "password";
    private final SessionManager sessionManager = new SessionManager();

    @Override
    public HttpResponseEntity service(final HttpRequest httpRequest) throws IOException {
        HttpRequestStartLine httpRequestStartLine = httpRequest.getHttpRequestStartLine();
        HttpRequestHeader httpRequestHeader = httpRequest.getHttpRequestHeader();
        HttpRequestBody httpRequestBody = httpRequest.getHttpRequestBody();

        HttpMethod httpMethod = httpRequestStartLine.getHttpMethod();
        String requestTarget = httpRequestStartLine.getPath();
        String account = httpRequestBody.findBodyValue(ACCOUNT_FIELD);

        if (httpMethod == HttpMethod.GET && account == null) {
            HttpCookie cookie = httpRequestHeader.getCookie();
            Session session = sessionManager.findSession(cookie.getJSessionId());

            // session이 존재한경우 = 로그인 한경우
            if (session != null) {
                return HttpResponseEntity
                        .builder()
                        .httpStatus(HttpStatus.FOUND)
                        .contentType(generateContentType(requestTarget))
                        .responsePage(INDEX_PAGE_URI)
                        .build();
            }

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
            return handleLoginFail(requestTarget);
        }

        return handleLoginSuccess(requestTarget);
    }

    private HttpResponseEntity handleLoginSuccess(final String requestTarget) {

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

    private HttpResponseEntity handleLoginFail(final String requestTarget) {
        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .contentType(generateContentType(requestTarget))
                .responsePage(UNAUTHORIZED_PAGE_URI)
                .build();
    }

    private User findAccount(final String account) {
        return InMemoryUserRepository.findByAccount(account).orElseThrow(NoSuchElementException::new);
    }
}
