package com.demo.mobilebank.account.exception;

import com.demo.mobilebank.account.bean.response.ErrorMessage;

public interface StandardError {
    String getErrorCode();
    String getMessage();

    default ErrorMessage getErrorMessage() {
        return new ErrorMessage(getMessage(), getErrorCode());
    }
}
