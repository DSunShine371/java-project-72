package gg.jte.generated.ondemand;
import hexlet.code.dto.BasePage;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,3,3,6,6,21,21,21,21,21,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"container-fluid bg-dark p-5 text-white\">\r\n        <div class=\"container\">\r\n            <h1 class=\"display-4\">Анализатор страниц</h1>\r\n            <p class=\"lead\">Бесплатно проверьте сайты на SEO пригодность</p>\r\n            <form action=\"/urls\" method=\"post\" class=\"d-flex justify-content-center\">\r\n                <div class=\"form-group me-3 w-75\">\r\n                    <label for=\"url-input\" class=\"visually-hidden\">Ссылка</label>\r\n                    <input type=\"text\" name=\"url\" id=\"url-input\" class=\"form-control form-control-lg\" placeholder=\"Ссылка\">\r\n                </div>\r\n                <button type=\"submit\" class=\"btn btn-primary btn-lg\">Проверить</button>\r\n            </form>\r\n            <p class=\"mt-2 text-center text-muted\">Пример: https://www.example.com</p>\r\n        </div>\r\n    </div>\r\n");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
