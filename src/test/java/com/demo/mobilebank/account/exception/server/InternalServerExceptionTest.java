package com.demo.mobilebank.account.exception.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InternalServerExceptionTest {

    @Test
    void test_getErrorCode() {
        final InternalServerException serverException = new InternalServerException("Some internal server error.");
        assertEquals("SERVER_ERROR", serverException.getErrorCode());
    }
}