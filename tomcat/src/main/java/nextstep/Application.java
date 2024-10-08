package nextstep;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("START - WEB - SERVER");
        final var tomcat = new Tomcat();
        tomcat.start();
    }
}
