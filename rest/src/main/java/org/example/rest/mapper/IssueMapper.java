package org.example.rest.mapper;


import org.example.rest.domain.Issue;
import org.example.rest.dto.*;
import org.mapstruct.Mapper;


import java.time.Instant;
import java.util.List;


// TODO: may be add to all response DTO owner id info
@Mapper(imports = Instant.class)
public interface IssueMapper {
    IssueGetAllRS toIssueGetAllRS(Issue item);


    List<IssueGetAllRS> toIssueGetAllRSList(List<Issue> item);


    IssueGetByIdRS toIssueGetByIdRS(Issue item);


    //    @Mapping(target = "id", constant = "0L")  0L
    //    @Mapping(target = "closed", constant = "false")  false
    //    @Mapping(target = "created", expression = "java(Instant.now())")  null
    Issue fromIssueCreateRQ(IssueCreateRQ dto, long ownerId);


    IssueCreateRS toIssueCreateRS(Issue item);


    Issue fromIssueUpdateByIdRQ(IssueUpdateByIdRQ dto, long ownerId);


    IssueUpdateByIdRS toIssueUpdateByIdRS(Issue item);
}


