package medical.pdp.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTransientConnectionException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(SQLTransientConnectionException.class)
    public void sqlTrigger() {}

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public void sqlConstraint() {}
}
