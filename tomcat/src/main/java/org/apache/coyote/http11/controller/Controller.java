package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseEntity;

import java.io.IOException;

public interface Controller {

    HttpResponseEntity service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;

    default ContentType generateContentType(final String requestTarget) {
        if (requestTarget.endsWith(".css")) {
            return ContentType.CSS;
        }
        return ContentType.HTML;
    }
}
