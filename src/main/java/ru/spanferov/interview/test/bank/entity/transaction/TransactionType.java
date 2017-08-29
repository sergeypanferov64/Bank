package ru.spanferov.interview.test.bank.entity.transaction;

public enum TransactionType {

    WITHDRAWAL(0),
    REFILL(1),
    TRANSFER(2);

    private short code;

    TransactionType(int code) {
        this.code = (short) code;
    }

    public short getCode() {
        return code;
    }
}
