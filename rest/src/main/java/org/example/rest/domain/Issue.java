package org.example.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Issue {
    private long id;
    private long ownerId;
    private String name;
    private String content;
    private boolean closed;
    private Instant created;
}
