package ru.spanferov.interview.test.bank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.spanferov.interview.test.bank.entity.Account;

public interface AccountService {

    Account read(Long id);

    Account readUserAccount(Long id);

    Page<Account> pageUserAccounts(Pageable pageable);

    Account withdraw(Long accountId, Long amount);

    Account refill(Long accountId, Long amount);

}
