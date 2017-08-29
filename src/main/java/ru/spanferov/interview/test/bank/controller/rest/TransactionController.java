package ru.spanferov.interview.test.bank.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.spanferov.interview.test.bank.entity.transaction.Refill;
import ru.spanferov.interview.test.bank.entity.transaction.Transaction;
import ru.spanferov.interview.test.bank.entity.transaction.Transfer;
import ru.spanferov.interview.test.bank.entity.transaction.Withdrawal;
import ru.spanferov.interview.test.bank.exception.BankException;
import ru.spanferov.interview.test.bank.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json"}, value = "/withdraw")
    public Transaction withdraw(@RequestBody Withdrawal withdrawal) {
        if (withdrawal.getAmount() == null || withdrawal.getFromAccountId() == null) {
            throw new BankException("Invalid request params");
        }
        return transactionService.withdraw(withdrawal);
    }

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json"}, value = "/refill")
    public Transaction refill(@RequestBody Refill refill) {
        if (refill.getAmount() == null || refill.getToAccountId() == null) {
            throw new BankException("Invalid request params");
        }
        return transactionService.refill(refill);
    }

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json"}, value = "/transfer")
    public Transaction transfer(@RequestBody Transfer transfer) {
        if (transfer.getAmount() == null || transfer.getFromAccountId() == null || transfer.getToAccountId() == null) {
            throw new BankException("Invalid request params");
        }
        return transactionService.transfer(transfer);
    }

}
