package org.example.rest.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

//запрос
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskCreateRQ {
    private String name;
    private Instant deadline;
    private Instant created;
}
