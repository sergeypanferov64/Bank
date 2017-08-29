package ru.spanferov.interview.test.bank.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.spanferov.interview.test.bank.entity.Account;
import ru.spanferov.interview.test.bank.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"}, value = "/{id}")
    public Account read(@PathVariable Long id) {
        return accountService.readUserAccount(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
    public Page<Account> page(@PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        return accountService.pageUserAccounts(pageable);
    }

}
