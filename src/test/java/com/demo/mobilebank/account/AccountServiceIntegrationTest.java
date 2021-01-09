package com.demo.mobilebank.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountServiceIntegrationTest {

    @Test
    void test_main() {
        AccountServiceApplication.main(new String[] {"--spring.profiles.active=test"});
        assertTrue(true);
    }
}
