package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private final Javalin app = App.getApp();

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Hello World");
        });
    }

    @Test
    void testNonExistingPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/non-existing-page");
            assertThat(response.code()).isEqualTo(404);
        });
    }
}
