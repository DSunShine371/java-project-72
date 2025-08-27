@import hexlet.code.dto.urls.UrlsPage
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
                <th>Дата создания</th>
            </tr>
            </thead>
            <tbody>
            @if (page.getUrls() == null || page.getUrls().isEmpty())
                <tr>
                    <td colspan="3" class="text-center">Сайтов пока нет</td>
                </tr>
            @else
                @for (var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                        <td>${url.getCreatedAt().toString()}</td>
                    </tr>
                @endfor
            @endif
            </tbody>
        </table>
    </div>
`
)