package org.example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IssueGetAllRS {
    private long id;
    private String name;
    private boolean closed;
    private Instant created;
}
