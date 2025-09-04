package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UrlCheckRepository extends BaseRepository {

    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DATA_SOURCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, urlCheck.getUrlId());
            stmt.setInt(2, urlCheck.getStatusCode());
            stmt.setString(3, urlCheck.getH1());
            stmt.setString(4, urlCheck.getTitle());
            stmt.setString(5, urlCheck.getDescription());
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    urlCheck.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB did not return a generated key.");
                }
            }
        }
    }

    public static List<UrlCheck> findAllByUrlId(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id DESC";
        try (Connection conn = DATA_SOURCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                List<UrlCheck> checks = new ArrayList<>();
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    int statusCode = resultSet.getInt("status_code");
                    String h1 = resultSet.getString("h1");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    Timestamp createdAt = resultSet.getTimestamp("created_at");

                    UrlCheck check = new UrlCheck(statusCode, title, h1, description, urlId);
                    check.setId(id);
                    check.setCreatedAt(createdAt);
                    checks.add(check);
                }
                return checks;
            }
        }
    }

    public static Optional<UrlCheck> findLatestCheckByUrlId(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = DATA_SOURCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    int statusCode = rs.getInt("status_code");
                    String h1 = rs.getString("h1");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    UrlCheck check = new UrlCheck(statusCode, title, h1, description, urlId);
                    check.setId(id);
                    check.setCreatedAt(createdAt);
                    return Optional.of(check);
                }
                return Optional.empty();
            }
        }
    }
}
