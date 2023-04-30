package org.example.rest.dto.task;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskUpdateByIdRQ {

    private long id;
    private String name;
    private Instant deadline;
    private boolean done;
}


