package com.concrete.solution.java.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthorizedException extends RuntimeException {

    public UserNotAuthorizedException() {
        super("Usuário e/ou senha inválidos");
    }
}
