DROP TABLE IF EXISTS sites;

CREATE TABLE sites (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    response_code INT
);

DROP TABLE IF EXISTS checks;

CREATE TABLE checks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    response_code INT,
    title VARCHAR(255) NOT NULL,
    h1 text NOT NULL,
    description text NOT NULL,
    created_at TIMESTAMP NOT NULL,
);