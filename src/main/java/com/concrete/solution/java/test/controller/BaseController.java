package com.concrete.solution.java.test.controller;

import com.concrete.solution.java.test.exception.ErrorMessage;
import com.concrete.solution.java.test.exception.UserCreateException;
import com.concrete.solution.java.test.exception.UserNotAuthorizedException;
import com.concrete.solution.java.test.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.NoResultException;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Exception> handleNoResultException(NoResultException nre) {
        logger.error("> handleNoResultException");
        logger.error("- NoResultException: ", nre);
        logger.error("< handleNoResultException");
        return new ResponseEntity<Exception>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        logger.error("> handleException");
        logger.error("- Exception: ", e);
        logger.error("< handleException");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<ErrorMessage> handleUserNotAuthorizedException(Exception e) {
        logger.error("> handleException");
        logger.error("- Exception: ", e);
        logger.error("< handleException");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserCreateException.class)
    public ResponseEntity<ErrorMessage> handleUserCreateException(Exception e) {
        logger.error("> handleException");
        logger.error("- Exception: ", e);
        logger.error("< handleException");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(Exception e) {
        logger.error("> handleException");
        logger.error("- Exception: ", e);
        logger.error("< handleException");
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
