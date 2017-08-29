package ru.spanferov.interview.test.bank.service;

import ru.spanferov.interview.test.bank.entity.transaction.Refill;
import ru.spanferov.interview.test.bank.entity.transaction.Transaction;
import ru.spanferov.interview.test.bank.entity.transaction.Transfer;
import ru.spanferov.interview.test.bank.entity.transaction.Withdrawal;

public interface TransactionService {

    Transaction withdraw(Withdrawal t);

    Transaction refill(Refill t);

    Transaction transfer(Transfer t);

}
