package org.example.rest.service.task;


import lombok.RequiredArgsConstructor;

import org.example.rest.domain.task.Task;
import org.example.rest.dto.task.*;
import org.example.rest.exception.AppForbiddenException;
import org.example.rest.exception.AppNotFoundException;
import org.example.rest.mapper.task.TaskMapper;
import org.example.rest.security.AppAuthentication;
import org.example.rest.security.AppUserDetails;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskMapper mapper; // <- @Autowired from constructor
    //    private final List<Task> items = new ArrayList<>(256);
//    private long nextId = 1;
    private final NamedParameterJdbcOperations operations;
    private final AppAuthentication authentication;
    private final RowMapper<Task> rowMapper = (rs, rowNum) ->
            new Task(rs.getLong("id"),
                    rs.getLong("owner_id"),
                    rs.getString("name"),
                    rs.getObject("deadline", OffsetDateTime.class).toInstant(),
                    rs.getObject("created", OffsetDateTime.class).toInstant(),
                    rs.getBoolean("done"));


    public List<TaskGetAllRS> getAll(int limit, int offset) {
        List<Task> item = this.operations.query(
                "SELECT id," +
                        "name," +
                        "deadline," +
                        "created ," +
                        "done ," +
                        "FROM tasks" +
                        "ORDER BY id " +
                        "LIMIT :limit OFFSET :offset ",
                Map.of("limit", limit, "offset", offset), // forbid null - NPE
                rowMapper
        );
        return this.mapper.toTaskGetAllRSList(item);
    }



    public TaskGetByIdRS getById(long id) {
        return this.operations.query(
                        "SELECT id," +
                                "name," +
                                "deadline," +
                                "created ," +
                                "done ," +
                                "FROM tasks " +
                                "WHERE id = :id " +
                                "ORDER BY id LIMIT 1 ",
                        Map.of("id", id), // forbid null - NPE
                        rowMapper)
                .stream()
                .findFirst()
                .map(this.mapper::toTaskGetByIdRS)
                .orElseThrow(AppNotFoundException::new);
    }


    public TaskCreateRS create(@Valid TaskCreateRQ requestDTO) {
        if (this.authentication.isAnonymous()) {
            throw new AppForbiddenException();
        }
        Task task = this.mapper.fromTaskCreateRQ(requestDTO,
                authentication.getPrincipal().getId());

        return this.operations.query(
                        "INSERT INTO tasks (" +
                                "owner_id," +
                                "name," +
                                "deadline)" +
                                "VALUES(:owner_id, :name, :deadline)" +
                                "RETURNING id," +
                                "owner_id," +
                                "name," +
                                "deadline," +
                                "created," +
                                "done",
                        Map.of(
                                "owner_id", task.getOwnerId(),
                                "name", task.getName(),
                                "created", task.getCreated()
                        ), // forbid null - NPE
                        rowMapper
                )
                .stream()
                .findFirst()
                .map(this.mapper::toTaskCreateRS)
                .orElseThrow(AppNotFoundException::new);

    }

    public TaskUpdateByIdRS updateById(TaskUpdateByIdRQ requestDTO) {
        if (this.authentication.isAnonymous()){
            throw new AppForbiddenException();
        }

        AppUserDetails principal = this.authentication.getPrincipal();

        if (principal.getRoles().contains("ROLE_ADMIN")) {
            Task task = this.mapper.fromTaskUpdateByIdRQ(requestDTO, 0);


        return this.operations.query(
                        "UPDATE tasks SET" +
                                "name= :name, " +
                                "deadline = :deadline," +
                                "done = :done" +
                                "WHERE id = :id RETURNING " +
                                "id," +
                                "owner_id" +
                                "name," +
                                "deadline" +
                                "created," +
                                "done",
                        Map.of(
                                "id", task.getId(),
                                "name", task.getName(),
                                "deadline", Timestamp.from(task.getDeadline()),
                                "done", task.isDone()
                        ), /// TODO почему не могу дабавить поле? И почмуто сервер отрудается
                        rowMapper)
                .stream()
                .findFirst()
                .map(this.mapper::toTaskUpdateByIdRS)
                .orElseThrow(AppNotFoundException::new);
        }

        Task task = this.mapper.fromTaskUpdateByIdRQ(requestDTO,
                principal.getId());

        long actualOwnerId = this.operations.query("SELECT owner_id" +
                " FROM tasks " +
                "WHERE id = :id",
                        Map.of("id", task.getId()),
                        (rs, rowNum) -> rs.getLong("owner_id")
                ).stream()
                .findFirst()
                .orElseThrow(AppNotFoundException::new);

        if (actualOwnerId != principal.getId()) {
            throw new AppForbiddenException();
        }
        return this.operations.query(
                        "UPDATE tasks SET" +
                                "name = :name," +
                                "deadline = :deadline," +
                                "created = :created" +
                                "WHERE id = :id" +
                                "AND owner_id =:owner_id RETURNING" +
                                "id," +
                                "owner_id," +
                                "name, " +
                                "deadline," +
                                "created," +
                                "done",
                        Map.of(
                                "id", task.getId(),
                                "owner_id", principal.getId(),
                                "name", task.getName(),
                                "deadline", task.getName(),
                                "created", task.getCreated(),
                                "done", task.isDone()
                        ), // forbid null - NPE
                        rowMapper
                )
                .stream()
                .findFirst()
                .map(this.mapper::toTaskUpdateByIdRS)
                .orElseThrow(AppNotFoundException::new);
// TODO продолжить отсюда


//        private long id;
//        private long ownerId;
//        private String name;
//        private Instant deadline;
//        private Instant created;
//        private boolean done;
    }

// forbid null - NPE

    public void removeById(long id) {
        boolean removed = this.operations.update(
                "DELETE FROM tasks WHERE id = :id",
                Map.of("id", id)
        ) != 0;
        if (!removed) {
            throw new AppNotFoundException();
        }
    }


//    public void removeById(long id) {
//        boolean removed = this.items.removeIf(o -> o.getId() == id);
//        if (!removed) {
//            throw new AppNotFoundException();
//        }
//    }
}


