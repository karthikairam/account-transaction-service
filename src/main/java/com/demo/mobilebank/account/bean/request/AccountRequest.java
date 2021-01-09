package com.demo.mobilebank.account.bean.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String holderFirstName;
    @NotBlank
    @Size(min = 1, max = 50)
    private String holderLastName;
    @NotNull
    @DecimalMin("1.00")
    private BigDecimal balance;
}
