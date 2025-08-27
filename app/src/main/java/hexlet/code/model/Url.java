package hexlet.code.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }
}
