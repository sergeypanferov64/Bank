package ru.spanferov.interview.test.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spanferov.interview.test.bank.entity.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
