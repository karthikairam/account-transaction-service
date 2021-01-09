package com.demo.mobilebank.account.exception.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientExceptionTest {

    @Test
    void test_getErrorCode() {
        final ClientException clientException = new ClientException("Some Client Error");
        assertEquals("CLIENT_ERROR", clientException.getErrorCode());
    }
}