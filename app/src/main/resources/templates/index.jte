<!doctype html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Анализатор страниц</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr"
          crossorigin="anonymous">
</head>
<body class="d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Анализатор страниц</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="/">Главная</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/urls">Сайты</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="flex-grow-1">
    <div class="container-fluid bg-dark p-5 text-white">
        <div class="container">
            <h1 class="display-4">Анализатор страниц</h1>
            <p class="lead">Бесплатно проверьте сайты на SEO пригодность</p>
            <form action="/urls" method="post" class="d-flex justify-content-center">
                <div class="form-group me-3 w-75">
                    <label for="url-input" class="visually-hidden">Ссылка</label>
                    <input type="text" name="url" id="url-input" class="form-control form-control-lg" placeholder="Ссылка">
                </div>
                <button type="submit" class="btn btn-primary btn-lg">Проверить</button>
            </form>
            <p class="mt-2 text-center text-muted">Пример: https://www.example.com</p>
        </div>
    </div>
</main>

<footer class="footer mt-auto py-3 bg-light">
    <div class="container text-center">
        <span class="text-muted">created by <a href="https://hexlet.io" target="_blank">Hexlet</a></span>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous">
</script>
</body>
</html>