package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private Javalin app;

    @BeforeEach
    final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
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
