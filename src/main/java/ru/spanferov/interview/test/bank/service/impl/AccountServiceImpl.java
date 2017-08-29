package ru.spanferov.interview.test.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spanferov.interview.test.bank.entity.Account;
import ru.spanferov.interview.test.bank.exception.BankException;
import ru.spanferov.interview.test.bank.repository.AccountRepository;
import ru.spanferov.interview.test.bank.service.AccountService;

import static ru.spanferov.interview.test.bank.security.SecurityUtils.getCurrentLoggedUserName;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account read(Long id) {
        return accountRepository.findOne(id);
    }

    @Override
    public Account readUserAccount(Long id) {
        return accountRepository.findOneByAccountIdAndUserName(id, getCurrentLoggedUserName());
    }

    @Override
    public Page<Account> pageUserAccounts(Pageable pageable) {
        return accountRepository.pageByUserName(getCurrentLoggedUserName(), pageable);
    }

    /**
     * Non-synchronized method for decreased amount of account balance.
     *
     * @param accountId account number of the current logged user
     * @param amount    - amount in account currency
     * @return new state of account object
     */
    @Override
    @Transactional
    public Account withdraw(Long accountId, Long amount) {
        if (amount <= 0) {
            throw new BankException("The amount must be positive!");
        }
        Account a = accountRepository.findOneByAccountIdAndUserName(accountId, getCurrentLoggedUserName());
        if (a == null) {
            throw new BankException("Account not found!");
        }
        Long balance = a.getBalance();
        if (balance < amount) {
            throw new BankException("Insufficient funds in the account!");
        }
        a.setBalance(balance - amount);
        return accountRepository.saveAndFlush(a);
    }

    /**
     * Non-synchronized method for increased amount of account balance.
     *
     * @param accountId account number of any user
     * @param amount    - amount in account currency
     * @return new state of account object
     */
    @Override
    @Transactional
    public Account refill(Long accountId, Long amount) {
        if (amount <= 0) {
            throw new BankException("The amount must be positive!");
        }
        Account a = accountRepository.findOne(accountId);
        if (a == null) {
            throw new BankException("Account not found!");
        }
        a.setBalance(a.getBalance() + amount);
        return accountRepository.saveAndFlush(a);
    }

}
