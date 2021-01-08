package com.demo.mobilebank.account.controller;

import com.demo.mobilebank.account.bean.response.ErrorMessage;
import com.demo.mobilebank.account.exception.client.AccountNotFoundException;
import com.demo.mobilebank.account.exception.client.BadRequestException;
import com.demo.mobilebank.account.exception.server.InternalServerException;
import com.demo.mobilebank.account.exception.server.NoRollBackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage badRequestErrorHandler(final BadRequestException exception) {
        return exception.getErrorMessage();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorMessage notFoundErrorHandler(final AccountNotFoundException exception) {
        return exception.getErrorMessage();
    }

    @ExceptionHandler({InternalServerException.class, NoRollBackException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ErrorMessage internalServerErrorHandler(final InternalServerException exception) {
        return exception.getErrorMessage();
    }
}
