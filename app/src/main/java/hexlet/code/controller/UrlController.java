package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static hexlet.code.util.NamedRoutes.mainPagePath;
import static hexlet.code.util.NamedRoutes.urlPath;
import static hexlet.code.util.NamedRoutes.urlsPath;
import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {

    private final String flash = "flash";

    public void showMainPage(Context ctx) {
        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute(flash));
        ctx.render("index.jte", model("page", page));
    }

    public void createUrl(Context ctx) {
        String name = ctx.formParam("url");

        if (name == null || name.isBlank()) {
            ctx.sessionAttribute(flash, "URL не может быть пустым");
            ctx.redirect(mainPagePath());
            return;
        }

        try {
            URL parsedUrl = new URI(name).toURL();
            String normalizedUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getAuthority();

            if (UrlRepository.findByName(normalizedUrl).isPresent()) {
                ctx.sessionAttribute(flash, "Страница уже существует");
            } else {
                Url url = new Url(normalizedUrl);
                UrlRepository.save(url);
                ctx.sessionAttribute(flash, "Страница успешно добавлена");
            }
            ctx.redirect(urlsPath());
        } catch (MalformedURLException | URISyntaxException | SQLException | IllegalArgumentException e) {
            ctx.sessionAttribute(flash, "Некорректный URL");
            ctx.redirect(mainPagePath());
        }
    }

    public void indexUrls(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getAll();
        for (Url url : urls) {
            Optional<UrlCheck> lastCheckOptional = UrlCheckRepository.findLatestCheckByUrlId(url.getId());

            lastCheckOptional.ifPresent(check -> {
                url.setLastCheckCreatedAt(check.getCreatedAt());
                url.setLastCheckStatusCode(check.getStatusCode());
            });
        }

        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute(flash));
        ctx.render("urls/index.jte", model("page", page));
    }

    public void showUrl(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Optional<Url> url = UrlRepository.findById(id);
        if (url.isPresent()) {
            List<UrlCheck> urlChecks = UrlCheckRepository.findAllByUrlId(id);
            var page = new UrlPage(url.get(), urlChecks);
            page.setFlash(ctx.consumeSessionAttribute(flash));
            ctx.render("urls/show.jte", model("page", page));
        } else {
            throw new NotFoundResponse("URL not found");
        }
    }

    public void indexUrlChecks(Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse("URL не найден"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Integer statusCode = response.getStatus();

            String body = response.getBody();
            if (statusCode >= 400) {
                UrlCheck urlCheck = new UrlCheck(statusCode, "", "", "", urlId);
                UrlCheckRepository.save(urlCheck);
            } else {
                if (body == null || body.isBlank()) {
                    throw new IllegalStateException("Empty response body");
                }
                Document doc = Jsoup.parse(body);
                String title = doc.title();
                String h1 = Optional.ofNullable(doc.selectFirst("h1"))
                        .map(Element::text)
                        .orElse("");
                String description = Optional.ofNullable(doc.selectFirst("meta[name=description]"))
                        .map(element -> element.attr("content"))
                        .orElse("");

                UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                UrlCheckRepository.save(urlCheck);
            }

            ctx.sessionAttribute(flash, "Страница успешно проверена");
            ctx.redirect(urlPath(urlId));

        } catch (Exception e) {
            ctx.sessionAttribute(flash, "Не удалось проверить страницу");
            ctx.redirect(urlPath(urlId));
        }
    }
}
