package nextstep.org.apache.coyote.http11;

import org.apache.coyote.http11.controller.Controller;
import org.apache.coyote.http11.response.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ContentTypeTest {

    @Test
    void ContentType_변환_테스트() {
        Controller controller = mock(Controller.class);
        when(controller.generateContentType(anyString())).thenCallRealMethod();

        Assertions.assertThat(ContentType.HTML).isEqualTo(controller.generateContentType("index.html"));
    }
}
