package com.demo.mobilebank.account.service;

import com.demo.mobilebank.account.bean.request.MoneyTransactionRequest;
import com.demo.mobilebank.account.entity.MoneyTransaction;
import com.demo.mobilebank.account.entity.mapper.RequestMapper;
import com.demo.mobilebank.account.exception.server.InternalServerException;
import com.demo.mobilebank.account.exception.server.NoRollBackException;
import com.demo.mobilebank.account.repository.MoneyTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoneyTransactionServiceImpl implements MoneyTransactionService {

    private final MoneyTransactionRepository moneyTransactionRepository;
    private final AccountService accountService;
    private final RequestMapper<MoneyTransactionRequest, MoneyTransaction> moneyTransactionRequestMapper;

    @Override
    @Transactional(noRollbackFor = NoRollBackException.class)
    public MoneyTransaction transferMoney(final MoneyTransactionRequest moneyTxReq) {

        return Optional.of(moneyTxReq)
                .map(moneyTransactionRequestMapper::mapFrom)
                .map(moneyTransaction -> {
                    final MoneyTransaction outcomeMoneyTransaction;
                    try {
                        accountService
                                .makeFundTransfer(moneyTransaction.getFromAccountNumber(),
                                        moneyTransaction.getToAccountNumber(), moneyTransaction.getTransactionAmount());
                        moneyTransaction.setStatus("SUCCESS");
                    } catch (Exception e) {
                        moneyTransaction.setStatus("FAILED");
                        moneyTransaction.setErrorDescription(e.getMessage());
                        throw new NoRollBackException(e.getMessage());
                    } finally {
                        outcomeMoneyTransaction = moneyTransactionRepository.save(moneyTransaction);
                    }
                    return outcomeMoneyTransaction;
                })
                .orElseThrow(() -> new InternalServerException("Failure in request conversion."))
                ;
    }

}
