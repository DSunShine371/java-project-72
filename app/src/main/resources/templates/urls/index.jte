@import hexlet.code.util.DateFormatter
@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="container mt-4">
        <h1 class="mb-4">Сайты</h1>
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
                <th>Дата добавления</th>
            </tr>
            </thead>
            <tbody>
            @if (page.getUrls() == null || page.getUrls().isEmpty())
                <tr>
                    <td colspan="5" class="text-center">Сайтов пока нет</td>
                </tr>
            @else
                @for (var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td>
                            @if (url.getLastCheckTime().isPresent())
                                ${DateFormatter.formatTimestamp(url.getLastCheckTime().get())}
                            @endif
                        </td>
                        <td>
                            @if (url.getLastCheckStatus().isPresent())
                                ${url.getLastCheckStatus().get()}
                            @endif
                        </td>
                        <td>${DateFormatter.formatTimestamp(url.getCreatedAt())}</td>
                    </tr>
                @endfor
            @endif
            </tbody>
        </table>
    </div>
`
)