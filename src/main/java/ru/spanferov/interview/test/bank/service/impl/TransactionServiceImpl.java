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
    public Transaction withdraw(Withdrawal withdrawal) {
        synchronized (withdrawal.getFromAccountId()) {
            accountService.withdraw(withdrawal.getFromAccountId(), withdrawal.getAmount());
            withdrawal.setDateTime(LocalDateTime.now());
            return transactionRepository.save(withdrawal);
        }
    }

    @Override
    @Transactional
    public Transaction refill(Refill refill) {
        synchronized (refill.getToAccountId()) {
            accountService.refill(refill.getToAccountId(), refill.getAmount());
            refill.setDateTime(LocalDateTime.now());
            return transactionRepository.save(refill);
        }
    }

    @Override
    @Transactional
    public Transaction transfer(Transfer transfer) {

        Long firstAccountId = transfer.getFromAccountId();
        Long secondAccountId = transfer.getToAccountId();

        if (firstAccountId.compareTo(secondAccountId) < 0) {
            firstAccountId = transfer.getToAccountId();
            secondAccountId = transfer.getFromAccountId();
        }

        synchronized (firstAccountId) {
            synchronized (secondAccountId) {
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

    }

}
