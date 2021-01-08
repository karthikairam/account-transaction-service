package com.demo.mobilebank.account.exception.client;

import com.demo.mobilebank.account.exception.StandardError;

public class ClientException extends RuntimeException implements StandardError {

    public ClientException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "CLIENT_ERROR";
    }
}
