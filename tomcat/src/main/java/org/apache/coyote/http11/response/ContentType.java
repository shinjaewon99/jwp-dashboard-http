package org.apache.coyote.http11.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    HTML("text/html", "html"),
    CSS("text/css", "css"),
    JAVASCRIPT("text/javascript", "js"),
    SVG("image/svg+xml", "svg");

    private final String name;
    private final String extension;

    public static ContentType from(String requestTarget) {
        for (ContentType type : values()) {
            if (requestTarget.endsWith(type.getExtension())) {
                return type;
            }
        }
        return HTML;
    }
}
