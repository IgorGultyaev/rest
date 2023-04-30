package org.example.rest.error;


import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class AppErrorController extends AbstractErrorController {
    private final ErrorAttributes errorAttributes;


    public AppErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }


    @RequestMapping
    public ResponseEntity<ErrorRS> error(HttpServletRequest request, WebRequest webRequest) {
        HttpStatus status = getStatus(request);
        // for details BasicErrorController
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        Throwable error = this.errorAttributes.getError(webRequest);
        // error can be null
        return ResponseEntity.status(status).body(new ErrorRS("...", Map.of()));
    }
}
