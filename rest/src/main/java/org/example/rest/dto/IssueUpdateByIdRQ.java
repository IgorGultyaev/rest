package org.example.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rest.validation.ContainsLabel;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class IssueUpdateByIdRQ {
    @Min(1)
    private long id;
    @NotBlank
    @ContainsLabel({"bug", "feature", "backport"})
    private String name;
    @NotBlank
    private String content;
    private boolean closed;
}


