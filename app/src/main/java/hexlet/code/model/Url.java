package hexlet.code.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Optional;

@Data
public final class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;

    private Timestamp lastCheckCreatedAt;
    private Integer lastCheckStatusCode;

    public Url(String name) {
        this.name = name;
    }

    public Optional<Timestamp> getLastCheckTime() {
        return Optional.ofNullable(lastCheckCreatedAt);
    }

    public Optional<Integer> getLastCheckStatus() {
        return Optional.ofNullable(lastCheckStatusCode);
    }
}
