package com.concrete.solution.java.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserCreateException extends RuntimeException {

    public UserCreateException() {
        super("Usuário Existente!");
    }
}
