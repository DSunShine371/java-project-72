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
        } else {
            config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        }
        DATA_SOURCE = new HikariDataSource(config);
    }

    protected BaseRepository() { }
}
