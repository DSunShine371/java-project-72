package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
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

        try (var connection = BaseRepository.dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("DELETE FROM urls");
        }
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
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

    @Test
    void testUrlPage() throws SQLException {
        var url = new Url("https://www.example.com");
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/9999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    void testCreateValidUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.test.com";
            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlRepository.findByName("https://www.test.com")).isPresent();
        });
    }

    @Test
    void testCreateInvalidUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=invalid-url";
            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlRepository.findByName("invalid-url")).isNotPresent();
        });
    }

    @Test
    void testCreateDuplicateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.test-duplicate.com";
            client.post("/urls", requestBody);

            assertThat(UrlRepository.findByName("https://www.test-duplicate.com")).isPresent();

            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlRepository.getAll()).hasSize(1);
        });
    }
}
