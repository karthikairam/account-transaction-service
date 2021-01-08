package com.demo.mobilebank.account.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestHelper {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
