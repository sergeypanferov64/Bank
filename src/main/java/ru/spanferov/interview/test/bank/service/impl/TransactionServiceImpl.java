package ru.spanferov.interview.test.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spanferov.interview.test.bank.entity.Account;
import ru.spanferov.interview.test.bank.entity.transaction.*;
import ru.spanferov.interview.test.bank.exception.BankException;
import ru.spanferov.interview.test.bank.repository.TransactionRepository;
import ru.spanferov.interview.test.bank.service.AccountService;
import ru.spanferov.interview.test.bank.service.TransactionService;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public synchronized Transaction withdraw(Withdrawal withdrawal) {
        accountService.withdraw(withdrawal.getFromAccountId(), withdrawal.getAmount());
        withdrawal.setDateTime(LocalDateTime.now());
        return transactionRepository.save(withdrawal);
    }

    @Override
    @Transactional
    public synchronized Transaction refill(Refill refill) {
        accountService.refill(refill.getToAccountId(), refill.getAmount());
        refill.setDateTime(LocalDateTime.now());
        return transactionRepository.save(refill);
    }

    @Override
    @Transactional
    public synchronized Transaction transfer(Transfer transfer) {
        Account from = accountService.withdraw(transfer.getFromAccountId(), transfer.getAmount());
        Account to = accountService.read(transfer.getToAccountId());
        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new BankException("Sorry, transfer to an account with a different currency not implemented yet...");
        }
        accountService.refill(transfer.getToAccountId(), transfer.getAmount());
        transfer.setDateTime(LocalDateTime.now());
        return transactionRepository.save(transfer);
    }

}
