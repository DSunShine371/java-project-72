package hexlet.code.controller;

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

import java.sql.SQLException;
import java.util.Optional;

import static hexlet.code.util.NamedRoutes.urlPath;

public class UrlChecksController {

    private static final String FLASH = "flash";

    public void indexUrlChecks(Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse("URL не найден"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Integer statusCode = response.getStatus();

            Document doc = Jsoup.parse(response.getBody());
            String title = doc.title();
            String h1 = Optional.ofNullable(doc.selectFirst("h1"))
                    .map(Element::text)
                    .orElse("");
            String description = Optional.ofNullable(doc.selectFirst("meta[name=description]"))
                    .map(element -> element.attr("content"))
                    .orElse("");

            UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute(FLASH, "Страница успешно проверена");
            ctx.redirect(urlPath(urlId));

        } catch (Exception e) {
            ctx.sessionAttribute(FLASH, "Не удалось проверить страницу");
            ctx.redirect(urlPath(urlId));
        }
    }
}
