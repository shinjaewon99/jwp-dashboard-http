package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
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

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
    private static final String HTML_CONTENT_TYPE = "text/html";
    private static final String CSS_CONTENT_TYPE = "text/css";
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
            final String[] request = bufferedReader.readLine().split(" ");
            final String requestTarget = request[1];
            final String response = httpRequestHandler(requestTarget);

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String httpRequestHandler(final String requestTarget) throws IOException {

        // "/" 루트 경로일때
        if (requestTarget.equals("/")) {
            final var responseBody = "Hello world!";

            return createHttpResponse(requestTarget, responseBody);
        }

        // 루트 경로가 아닐경우
        URL resource = getClass()
                .getClassLoader()
                .getResource("static" + requestTarget);

        if (resource == null) {
            // 리소스를 찾지 못한 경우에 대한 처리
            throw new FileNotFoundException("Resource not found: " + requestTarget);
        }

        final String filePath = resource.getFile();
        final String responseBody = new String(Files.readAllBytes(Paths.get(filePath)));

        return createHttpResponse(requestTarget, responseBody);
    }

    private String createHttpResponse(final String requestTarget, final String responseBody) {
        return String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: " + loadContentType(requestTarget) + ";charset=utf-8 ",
                "Content-Length: " + responseBody.getBytes().length + " ",
                "",
                responseBody);
    }

    private String loadContentType(final String requestTarget) {
        if (requestTarget.endsWith(".css")) {
            return CSS_CONTENT_TYPE;
        }
        return HTML_CONTENT_TYPE;
    }
}
