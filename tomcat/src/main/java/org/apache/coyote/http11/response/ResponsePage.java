package org.apache.coyote.http11.response;

import lombok.Getter;

@Getter
public enum ResponsePage {
    LOGIN_PAGE_URI("/login.html"),
    UNAUTHORIZED_PAGE_URI("/401.html"),
    INDEX_PAGE_URI("/index.html"),
    REGISTER_PAGE_URI("/register.html"),
    EMPTY("");
    private final String htmlUri;

    ResponsePage(String htmlUri) {
        this.htmlUri = htmlUri;
    }
}
