//package org.example.rest.service;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.rest.domain.Issue;
//import org.example.rest.dto.*;
//import org.example.rest.exception.AppForbiddenException;
//import org.example.rest.exception.AppNotFoundException;
//import org.example.rest.mapper.IssueMapper;
//import org.example.rest.security.AppAuthentication;
//import org.example.rest.security.AppUserDetails;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;
//
//
//import javax.validation.Valid;
//import java.time.OffsetDateTime;
//import java.util.List;
//import java.util.Map;
//
//
//@Slf4j
//@Service
//@Validated
//@RequiredArgsConstructor
//public class IssueService {
//    private final IssueMapper mapper; // <- @Autowired from constructor
//    private final NamedParameterJdbcOperations operations;
//    private final AppAuthentication authentication;
//    private final RowMapper<Issue> rowMapper = (rs, rowNum) ->
//            new Issue(rs.getLong("id"),
//                    rs.getLong("owner_id"),
//                    rs.getString("name"),
//                    rs.getString("content"),
//                    rs.getBoolean("closed"),
//                    rs.getObject("created", OffsetDateTime.class).toInstant());
//
//
//    public List<IssueGetAllRS> getAll(int limit, int offset) {
//        // executeQuery <-
//        // executeUpdate
//        // execute
//        List<Issue> items = this.operations.query(
//                "SELECT \"id\"," +
//                        " \"owner_id\"," +
//                        " \"name\", '...'" +
//                        " AS \"content\", \"closed\", \"created\" FROM \"issues\" ORDER BY \"id\" LIMIT :limit OFFSET :offset",
//                Map.of("limit", limit, "offset", offset), // forbid null - NPE
//                rowMapper
//        );
//        return this.mapper.toIssueGetAllRSList(items);
//    }
//
//
//    public IssueGetByIdRS getById(long id) {
//        return this.operations.query(
//                        "SELECT \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\" FROM \"issues\" WHERE \"id\" = :id ORDER BY \"id\" LIMIT 1",
//                        Map.of("id", id), // forbid null - NPE
//                        rowMapper
//                )
//                .stream()
//                .findFirst()
//                .map(this.mapper::toIssueGetByIdRS)
//                .orElseThrow(AppNotFoundException::new);
//    }
//
//
//    public IssueCreateRS create(@Valid IssueCreateRQ requestDTO) {
//        // fail fast
//        if (this.authentication.isAnonymous()) {
//            throw new AppForbiddenException();
//        }
//
//
//        Issue issue = this.mapper.fromIssueCreateRQ(requestDTO, authentication.getPrincipal().getId());
//
//
//        // id - by service (UUID)
//        // id - by DB:
//        //  - nextval ->
//        //  - SQL: insert/update/delete -> return WITH inserted AS (INSERT ...) SELECT * FROM inserted
//        return this.operations.query(
//                        "INSERT INTO \"issues\" (\"owner_id\", \"name\", \"content\") VALUES(:owner_id, :name, :content) RETURNING \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\"",
//                        Map.of(
//                                "owner_id", issue.getOwnerId(),
//                                "name", issue.getName(),
//                                "content", issue.getContent()
//                        ), // forbid null - NPE
//                        rowMapper
//                )
//                .stream()
//                .findFirst()
//                .map(this.mapper::toIssueCreateRS)
//                .orElseThrow(AppNotFoundException::new);
//    }
//
//
//    public IssueUpdateByIdRS updateById(IssueUpdateByIdRQ requestDTO) {
//        if (this.authentication.isAnonymous()) {
//            throw new AppForbiddenException();
//        }
//
//
//        AppUserDetails principal = this.authentication.getPrincipal();
//
//
//        if (principal.getRoles().contains("ROLE_ADMIN")) {
//            Issue issue = this.mapper.fromIssueUpdateByIdRQ(requestDTO, 0);
//
//
//            return this.operations.query(
//                            "UPDATE \"issues\" SET \"name\" = :name, \"content\" = :content, \"closed\" = :closed WHERE \"id\" = :id RETURNING \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\"",
//                            Map.of(
//                                    "id", issue.getId(),
//                                    "name", issue.getName(),
//                                    "content", issue.getContent(),
//                                    "closed", issue.isClosed()
//                            ), // forbid null - NPE
//                            rowMapper
//                    )
//                    .stream()
//                    .findFirst()
//                    .map(this.mapper::toIssueUpdateByIdRS)
//                    .orElseThrow(AppNotFoundException::new);
//        }
//
//
//        Issue issue = this.mapper.fromIssueUpdateByIdRQ(requestDTO,
//                principal.getId());
//
//
//        long actualOwnerId = this.operations.query("SELECT \"owner_id\" FROM \"issues\" WHERE \"id\" = :id",
//                        Map.of("id", issue.getId()),
//                        (rs, rowNum) -> rs.getLong("owner_id")
//                ).stream()
//                .findFirst()
//                .orElseThrow(AppNotFoundException::new);
//
//
//        if (actualOwnerId != principal.getId()) {
//            throw new AppForbiddenException();
//        }
//
//
//        return this.operations.query(
//                        "UPDATE \"issues\" SET \"name\" = :name, \"content\" = :content, \"closed\" = :closed WHERE \"id\" = :id AND \"owner_id\" = :owner_id RETURNING \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\"",
//                        Map.of(
//                                "id", issue.getId(),
//                                "owner_id", principal.getId(),
//                                "name", issue.getName(),
//                                "content", issue.getContent(),
//                                "closed", issue.isClosed()
//                        ), // forbid null - NPE
//                        rowMapper
//                )
//                .stream()
//                .findFirst()
//                .map(this.mapper::toIssueUpdateByIdRS)
//                .orElseThrow(AppNotFoundException::new);
//    }
//
//
//    public void removeById(long id) {
//        if (this.authentication.isAnonymous()) {
//            throw new AppForbiddenException();
//        }
//
//
//        AppUserDetails principal = this.authentication.getPrincipal();
//
//
//        if (principal.getRoles().contains("ROLE_ADMIN")) {
//            boolean removed = this.operations.update(
//                    "DELETE FROM \"issues\" WHERE \"id\" = :id",
//                    Map.of(
//                            "id", id
//                    )
//            ) != 0;
//            if (!removed) {
//                throw new AppNotFoundException();
//            }
//            return;
//        }
//
//
//        long actualOwnerId = this.operations.query("SELECT \"owner_id\" FROM \"issues\" WHERE \"id\" = :id",
//                        Map.of("id", id),
//                        (rs, rowNum) -> rs.getLong("owner_id")
//                ).stream()
//                .findFirst()
//                .orElseThrow(AppNotFoundException::new);
//
//
//        if (actualOwnerId != principal.getId()) {
//            throw new AppForbiddenException();
//        }
//
//
//        boolean removed = this.operations.update(
//                "DELETE FROM \"issues\" WHERE \"id\" = :id AND \"owner_id\" = :owner_id",
//                Map.of("id", id, "owner_id", principal.getId())
//        ) != 0;
//        if (!removed) {
//            throw new AppNotFoundException();
//        }
//    }
//}
//
//
//
//


package org.example.rest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rest.domain.Issue;
import org.example.rest.dto.*;
import org.example.rest.exception.AppForbiddenException;
import org.example.rest.exception.AppNotFoundException;
import org.example.rest.mapper.IssueMapper;
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
public class IssueService {
    private final IssueMapper mapper; // <- @Autowired from constructor
    private final NamedParameterJdbcOperations operations;
    private final AppUserDetails userDetails;
    private final RowMapper<Issue> rowMapper = (rs, rowNum) -> new Issue(rs.getLong("id"), rs.getLong("owner_id"), rs.getString("name"), rs.getString("content"), rs.getBoolean("closed"), rs.getObject("created", OffsetDateTime.class).toInstant());


    public List<IssueGetAllRS> getAll(int limit, int offset) {
        // executeQuery <-
        // executeUpdate
        // execute
        List<Issue> items = this.operations.query(
                "SELECT \"id\", \"owner_id\", \"name\", '...' AS \"content\", \"closed\", \"created\" FROM \"issues\" ORDER BY \"id\" LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), // forbid null - NPE
                rowMapper
        );
        return this.mapper.toIssueGetAllRSList(items);
    }


    public IssueGetByIdRS getById(long id) {
        return this.operations.query(
                        "SELECT \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\" FROM \"issues\" WHERE \"id\" = :id ORDER BY \"id\" LIMIT 1",
                        Map.of("id", id), // forbid null - NPE
                        rowMapper
                )
                .stream()
                .findFirst()
                .map(this.mapper::toIssueGetByIdRS)
                .orElseThrow(AppNotFoundException::new);
    }


    public IssueCreateRS create(@Valid IssueCreateRQ requestDTO) {
        // fail fast
        // TODO: add isAnonymous to AppUserDetails
        if (this.userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            throw new AppForbiddenException();
        }


        Issue issue = this.mapper.fromIssueCreateRQ(requestDTO, this.userDetails.getId());


        // id - by service (UUID)
        // id - by DB:
        //  - nextval ->
        //  - SQL: insert/update/delete -> return WITH inserted AS (INSERT ...) SELECT * FROM inserted
        return this.operations.query(
                        "INSERT INTO \"issues\" (\"owner_id\", \"name\", \"content\") VALUES(:owner_id, :name, :content) RETURNING \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\"",
                        Map.of(
                                "owner_id", issue.getOwnerId(),
                                "name", issue.getName(),
                                "content", issue.getContent()
                        ), // forbid null - NPE
                        rowMapper
                )
                .stream()
                .findFirst()
                .map(this.mapper::toIssueCreateRS)
                .orElseThrow(AppNotFoundException::new);
    }


    public IssueUpdateByIdRS updateById(IssueUpdateByIdRQ requestDTO) {
        // TODO: add isAnonymous to AppUserDetails
        if (this.userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            throw new AppForbiddenException();
        }


        // TODO: refactor this to Authorities
        if (this.userDetails.getRoles().contains("ROLE_ADMIN")) {
            Issue issue = this.mapper.fromIssueUpdateByIdRQ(requestDTO, 0);


            return this.operations.query(
                            "UPDATE \"issues\" SET \"name\" = :name, \"content\" = :content, \"closed\" = :closed WHERE \"id\" = :id RETURNING \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\"",
                            Map.of(
                                    "id", issue.getId(),
                                    "name", issue.getName(),
                                    "content", issue.getContent(),
                                    "closed", issue.isClosed()
                            ), // forbid null - NPE
                            rowMapper
                    )
                    .stream()
                    .findFirst()
                    .map(this.mapper::toIssueUpdateByIdRS)
                    .orElseThrow(AppNotFoundException::new);
        }


        Issue issue = this.mapper.fromIssueUpdateByIdRQ(requestDTO, this.userDetails.getId());


        long actualOwnerId = this.operations.query("SELECT \"owner_id\" FROM \"issues\" WHERE \"id\" = :id",
                        Map.of("id", issue.getId()),
                        (rs, rowNum) -> rs.getLong("owner_id")
                ).stream()
                .findFirst()
                .orElseThrow(AppNotFoundException::new);


        if (actualOwnerId != this.userDetails.getId()) {
            throw new AppForbiddenException();
        }


        return this.operations.query(
                        "UPDATE \"issues\" SET \"name\" = :name, \"content\" = :content, \"closed\" = :closed WHERE \"id\" = :id AND \"owner_id\" = :owner_id RETURNING \"id\", \"owner_id\", \"name\", \"content\", \"closed\", \"created\"",
                        Map.of(
                                "id", issue.getId(),
                                "owner_id", this.userDetails.getId(),
                                "name", issue.getName(),
                                "content", issue.getContent(),
                                "closed", issue.isClosed()
                        ), // forbid null - NPE
                        rowMapper
                )
                .stream()
                .findFirst()
                .map(this.mapper::toIssueUpdateByIdRS)
                .orElseThrow(AppNotFoundException::new);
    }


    public void removeById(long id) {
        // TODO: add isAnonymous to AppUserDetails
        if (this.userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            throw new AppForbiddenException();
        }


        // TODO: refactor this
        if (this.userDetails.getRoles().contains("ROLE_ADMIN")) {
            boolean removed = this.operations.update(
                    "DELETE FROM \"issues\" WHERE \"id\" = :id",
                    Map.of(
                            "id", id
                    )
            ) != 0;
            if (!removed) {
                throw new AppNotFoundException();
            }
            return;
        }


        long actualOwnerId = this.operations.query("SELECT \"owner_id\" FROM \"issues\" WHERE \"id\" = :id",
                        Map.of("id", id),
                        (rs, rowNum) -> rs.getLong("owner_id")
                ).stream()
                .findFirst()
                .orElseThrow(AppNotFoundException::new);


        if (actualOwnerId != this.userDetails.getId()) {
            throw new AppForbiddenException();
        }


        boolean removed = this.operations.update(
                "DELETE FROM \"issues\" WHERE \"id\" = :id AND \"owner_id\" = :owner_id",
                Map.of("id", id, "owner_id", this.userDetails.getId())
        ) != 0;
        if (!removed) {
            throw new AppNotFoundException();
        }
    }
}


