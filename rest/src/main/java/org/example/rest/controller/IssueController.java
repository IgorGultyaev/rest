package org.example.rest.controller;


import lombok.RequiredArgsConstructor;
import org.example.rest.dto.*;
import org.example.rest.service.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


@RestController // @Controller + @ResponseBody
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@Validated
public class IssueController {
    private final IssueService service; // DI <- @Autowired on constructor


    @GetMapping // GET /api/issues?offset=0&limit=100
    public List<IssueGetAllRS> getAll(
            @RequestParam(defaultValue = "100")
            @Min(1)
            @Max(100)
            int limit, // arg0
            @RequestParam(defaultValue = "0")
            @Min(0)
            int offset
    ) {
        return this.service.getAll(limit, offset);
    }


    @GetMapping("/{issueId}") // /api/issues/1, /api/issues/2
    public IssueGetByIdRS getById(
            @PathVariable
            @Min(1)
            long issueId
    ) {
        return this.service.getById(issueId);
    }


    @PostMapping // POST /api/issues
    public IssueCreateRS create(
            @RequestBody
            @Valid
            IssueCreateRQ requestDTO
    ) {
        return this.service.create(requestDTO);
    }


    @PutMapping
    public IssueUpdateByIdRS updateById(
            @RequestBody
            @Valid
            IssueUpdateByIdRQ requestDTO
    ) {
        return this.service.updateById(requestDTO);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{issueId}")
    public void removeById(
            @PathVariable
            @Min(1)
            long issueId
    ) {
        this.service.removeById(issueId);
    }
}


