package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class StaticResourceController implements Controller {
    private static final String CSS = ".css";

    @Override
    public HttpResponseEntity service(final HttpRequest httpRequest, final HttpResponse httpResponse) throws IOException {

        final String requestTarget = httpRequest.getHttpRequestStartLine().getPath();
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

    private ContentType generateContentType(final String requestTarget) {
        if (requestTarget.endsWith(CSS)) {
            return ContentType.CSS;
        }
        return ContentType.HTML;
    }
}
