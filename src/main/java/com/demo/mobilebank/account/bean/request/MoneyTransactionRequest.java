package com.demo.mobilebank.account.bean.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransactionRequest {
    private long fromAccountNumber;
    private long toAccountNumber;
    private BigDecimal transactionAmount;
    private String referenceNotes;
}
