package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.UrlChecksController;
import hexlet.code.database.DatabaseInitializer;
import hexlet.code.controller.UrlController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static hexlet.code.util.NamedRoutes.mainPagePath;
import static hexlet.code.util.NamedRoutes.urlChecksPath;
import static hexlet.code.util.NamedRoutes.urlPath;
import static hexlet.code.util.NamedRoutes.urlsPath;

@Slf4j
public class App {

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
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
        String sql = readResourceFile("schema.sql");

        log.info(sql);
        DatabaseInitializer.initializeSchema(sql);

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
            config.router.ignoreTrailingSlashes = true;
        });

        Unirest.config().reset();
        Unirest.config().connectTimeout(10000).socketTimeout(10000);

        var urlController = new UrlController();
        var urlChecksController = new UrlChecksController();

        app.get(mainPagePath(), urlController::showMainPage);
        app.post(urlsPath(), urlController::createUrl);
        app.get(urlsPath(), urlController::indexUrls);
        app.get(urlPath("{id}"), urlController::showUrl);
        app.post(urlChecksPath("{id}"), urlChecksController::indexUrlChecks);

        return app;
    }
}
