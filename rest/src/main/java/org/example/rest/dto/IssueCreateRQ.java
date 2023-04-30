package org.example.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rest.validation.ContainsLabel;


import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class IssueCreateRQ {
    @NotBlank
    @ContainsLabel({"bug", "feature", "backport"})
    private String name;
    @NotBlank
    private String content;
}


