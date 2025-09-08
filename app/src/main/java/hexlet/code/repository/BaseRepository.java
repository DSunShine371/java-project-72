package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class BaseRepository {
    protected static final HikariDataSource DATA_SOURCE;

    static {
        HikariConfig config = new HikariConfig();
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
        if (jdbcUrl != null && !jdbcUrl.isEmpty()) {
            config.setJdbcUrl(jdbcUrl);
            config.setDriverClassName("org.postgresql.Driver");
        } else {
            config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
            config.setDriverClassName("org.h2.Driver");
        }
        DATA_SOURCE = new HikariDataSource(config);
    }

    protected BaseRepository() { }
}
