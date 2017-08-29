package ru.spanferov.interview.test.bank.entity.transaction;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
@DiscriminatorValue(value = "1")
public class Refill extends Transaction {

    @Column(name = "to_account_id")
    private Long toAccountId;

    @PrePersist
    void init() {
        setTransactionType(TransactionType.REFILL.getCode());
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }
}
