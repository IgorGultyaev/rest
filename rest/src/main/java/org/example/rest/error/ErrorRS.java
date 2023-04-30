package org.example.rest.error;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorRS {
    private String code; // err.login_invalid
    private Map<String, String> errors;
}
