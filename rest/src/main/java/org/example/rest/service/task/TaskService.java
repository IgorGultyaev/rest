package org.example.rest.service.task;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rest.domain.task.Task;
import org.example.rest.dto.task.*;
import org.example.rest.exception.AppForbiddenException;
import org.example.rest.exception.AppNotFoundException;
import org.example.rest.mapper.task.TaskMapper;
import org.example.rest.security.AppUserDetails;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class TaskService {
    private final TaskMapper mapper; // <- @Autowired from constructor
    private final NamedParameterJdbcOperations operations;
    private final RowMapper<Task> rowMapper = (rs, rowNum) ->
            new Task(rs.getLong("id"),
                    rs.getLong("owner_id"),
                    rs.getString("name"),
                    rs.getObject("deadline", OffsetDateTime.class).toInstant(),
                    rs.getObject("created", OffsetDateTime.class).toInstant(),
                    rs.getBoolean("done"));
    private AppUserDetails userDetails;

    public List<TaskGetAllRS> getAll(int limit, int offset) {
        List<Task> item = this.operations.query(
                "SELECT id," +
                        "name," +
                        "deadline," +
                        "created ," +
                        "done ," +
                        "AS content, closed, created" +
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
                                "AS content, closed, created" +
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
        if (this.userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            throw new AppForbiddenException();
        }
        Task task = this.mapper.fromTaskCreateRQ(requestDTO, this.userDetails.getId());

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

    //TODO Изменять может только автор (владелец)
    public TaskUpdateByIdRS updateById(TaskUpdateByIdRQ requestDTO) {

        Task task = this.mapper.fromTaskUpdateByIdRQ(requestDTO, this.userDetails.getId());

        long actualOwnerId = this.operations.query("SELECT owner_id" +
                                " FROM tasks " +
                                "WHERE id = :id",
                        Map.of("id", task.getId()),
                        (rs, rowNum) -> rs.getLong("owner_id")
                ).stream()
                .findFirst()
                .orElseThrow(AppNotFoundException::new);

        if (actualOwnerId == this.userDetails.getId()) {
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
                                    "owner_id", userDetails.getId(),
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
        } else {
            throw new AppForbiddenException();
        }


    }


    // forbid null - NPE
// TODO Удалять может только автор (владелец) или админ (роль админ)
    public void removeById(long id) {

        long actualOwnerId = this.operations.query("SELECT owner_id FROM tasks WHERE id = :id",
                        Map.of("id", id),
                        (rs, rowNum) -> rs.getLong("owner_id")
                ).stream()
                .findFirst()
                .orElseThrow(AppNotFoundException::new);

        if ((this.userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) |
                (actualOwnerId == this.userDetails.getId())) {
            boolean removed = this.operations.update(
                    "DELETE FROM tasks WHERE id = :id",
                    Map.of(
                            "id", id
                    )
            ) != 0;
            if (!removed) {
                throw new AppNotFoundException();
            }
            return;
        } else {
            throw new AppNotFoundException();
        }
    }

}


