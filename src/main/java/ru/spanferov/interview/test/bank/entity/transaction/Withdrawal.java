package ru.spanferov.interview.test.bank.entity.transaction;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
@DiscriminatorValue(value = "0")
public class Withdrawal extends Transaction {

    @Column(name = "from_account_id")
    private Long fromAccountId;

    @PrePersist
    void init() {
        setTransactionType(TransactionType.WITHDRAWAL.getCode());
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
}
