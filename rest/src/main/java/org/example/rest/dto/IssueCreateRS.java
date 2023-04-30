package org.example.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Data

public class IssueCreateRS {
    private long id;
    private String name;
    private String content;
    private boolean closed;
    private Instant created;
}


