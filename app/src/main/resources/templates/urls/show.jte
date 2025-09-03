@import hexlet.code.util.DateFormatter
@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="container mt-4">
        <h1 class="mb-4">Сайт: ${page.getUrl().getName()}</h1>
        <table class="table table-bordered table-hover mt-3">
            <thead class="table-dark">
            <tr>
                <th scope="col">Свойство</th>
                <th scope="col">Значение</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <th scope="row">ID</th>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <th scope="row">Имя</th>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <th scope="row">Дата создания</th>
                <td>${DateFormatter.formatTimestamp(page.getUrl().getCreatedAt())}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="mt-5 mb-4">Проверки</h2>
        <div class="mb-3">
            <form action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" method="post">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
        </div>
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Код ответа</th>
                <th scope="col">title</th>
                <th scope="col">h1</th>
                <th scope="col">description</th>
                <th scope="col">Дата проверки</th>
            </tr>
            </thead>
            <tbody>
            @if (page.getUrlChecks() != null && !page.getUrlChecks().isEmpty())
                @for (var check : page.getUrlChecks())
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle()}</td>
                        <td>${check.getH1()}</td>
                        <td>${check.getDescription()}</td>
                        <td>${DateFormatter.formatTimestamp(check.getCreatedAt())}</td>
                    </tr>
                @endfor
            @endif
            </tbody>
        </table>
    </div>
`
)