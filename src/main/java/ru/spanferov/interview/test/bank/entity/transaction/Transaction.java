package ru.spanferov.interview.test.bank.entity.transaction;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.DiscriminatorType.INTEGER;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "TRANSACTION")
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "TRANSACTION_TYPE", discriminatorType = INTEGER)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_type", nullable = false, insertable = false, updatable = false)
    private Short transactionType;

    /**
     * coins (1/100) of the basic monetary unit of account
     */
    @Column(name = "amount")
    private Long amount;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Short getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Short transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
