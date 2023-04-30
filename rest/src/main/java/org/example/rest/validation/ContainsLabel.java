package org.example.rest.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


// Retention -- SOURCE, CLASS, RUNTIME
// Target --
@Constraint(validatedBy = ContainsLabelValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ContainsLabel {


    String message() default "{org.example.rest.validation.ContainsLabel.message}";


    Class<?>[] groups() default { };


    Class<? extends Payload>[] payload() default { };


    String[] value();
}



