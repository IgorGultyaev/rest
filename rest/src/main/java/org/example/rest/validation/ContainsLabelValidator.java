package org.example.rest.validation;


import lombok.RequiredArgsConstructor;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;


@RequiredArgsConstructor
public class ContainsLabelValidator implements ConstraintValidator<ContainsLabel, String> {
    private Set<String> labels;


    @Override
    public void initialize(ContainsLabel constraintAnnotation) {
        this.labels = Set.of(constraintAnnotation.value());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        // [bug] ...
        for (String label : labels) {
            if (value.startsWith("[" + label + "]")) {
                return true;
            }
        }
        return false;
    }
}


