package org.example.rest.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
    private long id;
    private long ownerId;
    private String name;
    private Instant deadline;
    private Instant created;
    private boolean done;
}




