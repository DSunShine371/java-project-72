package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Objects;

public class BaseRepository {
    protected static final HikariDataSource DATA_SOURCE;

    static {
        HikariConfig config = new HikariConfig();
        String jdbcUrl = System.getenv("DATABASE_URL");
        config.setJdbcUrl(Objects.requireNonNullElse(jdbcUrl, "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;"));
        DATA_SOURCE = new HikariDataSource(config);
    }

    protected BaseRepository() { }
}
