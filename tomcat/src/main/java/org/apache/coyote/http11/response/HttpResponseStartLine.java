package org.apache.coyote.http11.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.http11.common.HttpVersion;

@Getter
@RequiredArgsConstructor
public class HttpResponseStartLine {
    private final HttpVersion httpVersion;
    private final HttpResponseHeader httpResponseHeader;
}
