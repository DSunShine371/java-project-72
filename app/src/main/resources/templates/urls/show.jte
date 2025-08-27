@import hexlet.code.dto.urls.UrlPage
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
                <td>${page.getUrl().getCreatedAt().toString()}</td>
            </tr>
            </tbody>
        </table>
    </div>
`
)