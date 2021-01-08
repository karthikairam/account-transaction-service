package com.demo.mobilebank.account.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"accountNumber"})
@Entity
@SequenceGenerator(name="account_number_seq", initialValue=1000001, allocationSize=100)
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_number_seq")
    private long accountNumber;
    private String holderFirstName;
    private String holderLastName;
    private BigDecimal balance;
}
