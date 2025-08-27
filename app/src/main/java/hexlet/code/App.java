package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class App {

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();

        app.start(getPort());
    }

    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        String jdbcUrl = System.getenv("DATABASE_URL");
        String sql;

        if (jdbcUrl == null) {
            log.info("Using H2 database");
            hikariConfig.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
            sql = readResourceFile("schema.sql");
        } else {
            log.info("Using Postgres database");
            hikariConfig.setJdbcUrl(jdbcUrl);
            sql = readResourceFile("schema_pg.sql");
        }

        log.info(sql);
        var dataSource = new HikariDataSource(hikariConfig);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
            config.router.ignoreTrailingSlashes = true;
        });

        app.get("/", ctx -> {
            var page = new BasePage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("index.jte", model("page", page));
        });

        app.post("/urls", ctx -> {
            String name = ctx.formParam("url");

            if (name == null || name.isBlank()) {
                ctx.sessionAttribute("flash", "URL не может быть пустым");
                ctx.redirect("/");
                return;
            }

            try {
                URL parsedUrl = new URI(name).toURL();
                String normalizedUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getAuthority();

                if (UrlRepository.findByName(normalizedUrl).isPresent()) {
                    ctx.sessionAttribute("flash", "Страница уже существует");
                } else {
                    Url url = new Url(normalizedUrl);
                    UrlRepository.save(url);
                    ctx.sessionAttribute("flash", "Страница успешно добавлена");
                }
                ctx.redirect("/urls");
            } catch (MalformedURLException | URISyntaxException | SQLException | IllegalArgumentException e) {
                ctx.sessionAttribute("flash", "Некорректный URL");
                ctx.redirect("/");
            }
        });

        app.get("/urls", ctx -> {
            List<Url> urls = UrlRepository.getAll();
            var page = new UrlsPage(urls);
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("urls/index.jte", model("page", page));
        });

        app.get("/urls/{id}", ctx -> {
            long id = ctx.pathParamAsClass("id", Long.class).get();
            Optional<Url> url = UrlRepository.findById(id);
            if (url.isPresent()) {
                var page = new UrlPage(url.get());
                page.setFlash(ctx.consumeSessionAttribute("flash"));
                ctx.render("urls/show.jte", model("page", page));
            } else {
                throw new NotFoundResponse("URL not found");
            }
        });

        return app;
    }
}
