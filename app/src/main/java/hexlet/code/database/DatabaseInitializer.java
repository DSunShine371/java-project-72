package hexlet.code.database;

import hexlet.code.repository.BaseRepository;

import java.sql.SQLException;

public class DatabaseInitializer extends BaseRepository {
    public static void initializeSchema(String sql) throws SQLException {
        try (var connection = DATA_SOURCE.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
