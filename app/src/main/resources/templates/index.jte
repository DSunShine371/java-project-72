@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param BasePage page

@template.layout.page(
page = page,
content = @`
    <div class="container-fluid bg-dark p-5 text-white">
        <div class="container">
            <h1 class="display-4">Анализатор страниц</h1>
            <p class="lead">Бесплатно проверьте сайты на SEO пригодность</p>
            <form action="${NamedRoutes.urlsPath()}" method="post" class="d-flex justify-content-center">
                <div class="form-group me-3 w-75">
                    <label for="url-input" class="visually-hidden">Ссылка</label>
                    <input type="text" name="url" id="url-input" class="form-control form-control-lg" placeholder="Ссылка">
                </div>
                <button type="submit" class="btn btn-primary btn-lg">Проверить</button>
            </form>
            <p class="mt-2 text-center text-muted">Пример: https://www.example.com</p>
        </div>
    </div>
`
)