package org.example.rest.controller.task;

import lombok.RequiredArgsConstructor;
import org.example.rest.dto.task.*;
import org.example.rest.service.task.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService service;

    @GetMapping
    public List <TaskGetAllRS> getAll(
//            @RequestParam(defaultValue = "100") int limit,
//            @RequestParam(defaultValue = "0") int offset
            @RequestParam(defaultValue = "100")
            @Min(1)
            @Max(100)
            int limit,
            @RequestParam(defaultValue = "0")
            @Min(0)
            int offset
    ) {
        return this.service.getAll(limit, offset);
    }

    @GetMapping("/{taskID}")// /api/issues/1, /api/issues/2
    public TaskGetByIdRS getByIdRS(
//            @PathVariable long taskID
            @PathVariable
            @Min(1)
            long taskID
    ){
        return this.service.getById(taskID);
    }

    // GET /tasks?limit=10

    // POST /tasks
    // body: limit=10&offset=100
    @PostMapping
    public TaskCreateRS create(
            @RequestBody
            @Valid
            TaskCreateRQ requestDTO
    ) {
        return this.service.create(requestDTO);
    }


    @PutMapping // нельзя одновременно маппить два метода на одинаковые пути и одинаковые HTTP-методы, если не указаны ограничения
    public TaskUpdateByIdRS updateById(
            @RequestBody
            @Valid
            TaskUpdateByIdRQ requestDTO
    ) {
        return this.service.updateById(requestDTO);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{taskId}")
    public void removeById(
            @PathVariable
            @Min(1)
            long taskId
    )
    {
        this.service.removeById(taskId);
    }

}