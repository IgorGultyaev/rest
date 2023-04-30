package org.example.rest.mapper.task;

import org.example.rest.domain.Issue;
import org.example.rest.domain.task.Task;
import org.example.rest.dto.IssueCreateRQ;
import org.example.rest.dto.task.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

// TODO: may be add to all response DTO owner id info

@Mapper(imports = Instant.class)
public interface TaskMapper {
    TaskGetAllRS toTaskGetAllRS(Task item);

    List<TaskGetAllRS> toTaskGetAllRSList(List<Task> item);

    TaskGetByIdRS toTaskGetByIdRS(Task item);


    // @Mapping(target = "id", constant = "0L")
//    @Mapping(target = "deadline", constant = "2023-04-27T00:00:00Z")
//    @Mapping(target = "done", constant = "false")
//    @Mapping(target = "created", expression = "java(Instant.now())")
//    Task fromTaskCreateRQ(TaskCreateRQ dto);

    Task fromTaskCreateRQ(TaskCreateRQ dto, long ownerId);

    TaskCreateRS toTaskCreateRS(Task item);

    Task fromTaskUpdateByIdRQ(TaskUpdateByIdRQ dto, long ownerId);

    TaskUpdateByIdRS toTaskUpdateByIdRS(Task item);
}


