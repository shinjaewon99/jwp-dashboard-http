package org.apache.coyote.http11.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.request.HttpMethod;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.HttpRequestBody;
import org.apache.coyote.http11.request.HttpRequestStartLine;
import org.apache.coyote.http11.response.HttpResponseEntity;
import org.apache.coyote.http11.response.HttpStatus;

import java.io.IOException;

import static org.apache.coyote.http11.response.ResponsePage.INDEX_PAGE_URI;
import static org.apache.coyote.http11.response.ResponsePage.LOGIN_PAGE_URI;

public class RegisterController implements Controller {
    private static final String ACCOUNT_FIELD = "account";
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAIL_FIELD = "email";

    @Override
    public HttpResponseEntity service(final HttpRequest httpRequest) throws IOException {
        HttpRequestStartLine httpRequestStartLine = httpRequest.getHttpRequestStartLine();

        HttpMethod httpMethod = httpRequestStartLine.getHttpMethod();
        String requestTarget = httpRequestStartLine.getPath();
        HttpRequestBody httpRequestBody = httpRequest.getHttpRequestBody();

        String account = httpRequestBody.findBodyValue(ACCOUNT_FIELD);
        String password = httpRequestBody.findBodyValue(PASSWORD_FIELD);
        String email = httpRequestBody.findBodyValue(EMAIL_FIELD);

        if (httpMethod == HttpMethod.GET && account == null) {
            return HttpResponseEntity
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .contentType(generateContentType(requestTarget))
                    .responsePage(LOGIN_PAGE_URI)
                    .build();
        }

        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);

        return HttpResponseEntity
                .builder()
                .httpStatus(HttpStatus.FOUND)
                .contentType(generateContentType(requestTarget))
                .responsePage(INDEX_PAGE_URI)
                .build();
    }
}
