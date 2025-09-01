package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    final void setUp() throws IOException, SQLException {
        app = App.getApp();

        try (var connection = BaseRepository.dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("DELETE FROM url_checks");
            statement.execute("DELETE FROM urls");
        }
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
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

    @Test
    void testCheckUrl() {
        JavalinTest.test(app, (server, client) -> {
            String htmlBody = "<html><head><title>Test Page</title>"
                    + "<meta name=\"description\" content=\"Test description\"></head>"
                    + "<body><h1>Test H1</h1></body></html>";

            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "text/html")
                    .setBody(htmlBody);

            mockWebServer.enqueue(mockResponse);

            String mockUrlName = mockWebServer.url("/").toString();
            Url url = new Url(mockUrlName);
            UrlRepository.save(url);

            var response = client.post("/urls/" + url.getId() + "/checks");

            assertThat(response.code()).isEqualTo(200);

            Optional<UrlCheck> savedCheck = UrlCheckRepository.findLatestCheckByUrlId(url.getId());
            assertThat(savedCheck).isPresent();
            assertThat(savedCheck.get().getStatusCode()).isEqualTo(200);
            assertThat(savedCheck.get().getTitle()).isEqualTo("Test Page");
            assertThat(savedCheck.get().getH1()).isEqualTo("Test H1");
            assertThat(savedCheck.get().getDescription()).isEqualTo("Test description");
        });
    }

    @Test
    void testCheckUrlWithError() {
        JavalinTest.test(app, (server, client) -> {
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(404);

            mockWebServer.enqueue(mockResponse);

            String mockUrlName = mockWebServer.url("/").toString();
            Url url = new Url(mockUrlName);
            UrlRepository.save(url);

            var response = client.post("/urls/" + url.getId() + "/checks");

            assertThat(response.code()).isEqualTo(200);

            Optional<UrlCheck> savedCheck = UrlCheckRepository.findLatestCheckByUrlId(url.getId());
            assertThat(savedCheck).isPresent();
            assertThat(savedCheck.get().getStatusCode()).isEqualTo(404);
        });
    }
}
