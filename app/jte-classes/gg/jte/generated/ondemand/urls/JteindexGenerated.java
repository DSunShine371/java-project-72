package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.urls.UrlsPage;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,3,3,6,6,18,18,22,22,23,23,25,25,25,26,26,26,26,26,26,26,27,27,27,29,29,30,30,34,34,34,34,34,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"container mt-4\">\r\n        <h1 class=\"mb-4\">Сайты</h1>\r\n        <table class=\"table table-bordered table-hover\">\r\n            <thead class=\"table-dark\">\r\n            <tr>\r\n                <th>ID</th>\r\n                <th>Имя</th>\r\n                <th>Дата создания</th>\r\n            </tr>\r\n            </thead>\r\n            <tbody>\r\n            ");
				if (page.getUrls() == null || page.getUrls().isEmpty()) {
					jteOutput.writeContent("\r\n                <tr>\r\n                    <td colspan=\"3\" class=\"text-center\">Сайтов пока нет</td>\r\n                </tr>\r\n            ");
				} else {
					jteOutput.writeContent("\r\n                ");
					for (var url : page.getUrls()) {
						jteOutput.writeContent("\r\n                    <tr>\r\n                        <td>");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(url.getId());
						jteOutput.writeContent("</td>\r\n                        <td><a href=\"/urls/");
						jteOutput.setContext("a", "href");
						jteOutput.writeUserContent(url.getId());
						jteOutput.setContext("a", null);
						jteOutput.writeContent("\">");
						jteOutput.setContext("a", null);
						jteOutput.writeUserContent(url.getName());
						jteOutput.writeContent("</a></td>\r\n                        <td>");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(url.getCreatedAt().toString());
						jteOutput.writeContent("</td>\r\n                    </tr>\r\n                ");
					}
					jteOutput.writeContent("\r\n            ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
