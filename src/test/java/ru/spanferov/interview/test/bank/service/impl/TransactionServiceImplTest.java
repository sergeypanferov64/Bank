package ru.spanferov.interview.test.bank.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.spanferov.interview.test.bank.entity.Account;
import ru.spanferov.interview.test.bank.entity.transaction.Refill;
import ru.spanferov.interview.test.bank.entity.transaction.Transfer;
import ru.spanferov.interview.test.bank.entity.transaction.Withdrawal;
import ru.spanferov.interview.test.bank.service.AccountService;
import ru.spanferov.interview.test.bank.service.TransactionService;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class TransactionServiceImplTest {

    private final static int THREADS_NUMBER = 10;
    private final static int ITERATIONS_NUMBER = 10;

    private final static int DELAY_MIN = 10;
    private final static int DELAY_MAX = 30;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @TestConfiguration
    static class TransactionServiceImplTestContextConfiguration {

        @Bean
        public AccountService accountService() {
            return new AccountServiceImpl() {
                @Override
                public Account withdraw(Long accountId, Long amount) {
                    Random r = new Random();
                    try {
                        Thread.sleep(r.nextInt(DELAY_MAX - DELAY_MIN) + DELAY_MIN);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return super.withdraw(accountId, amount);
                }

                @Override
                public Account refill(Long accountId, Long amount) {
                    Random r = new Random();
                    try {
                        Thread.sleep(r.nextInt(DELAY_MAX - DELAY_MIN) + DELAY_MIN);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return super.refill(accountId, amount);
                }

                @Override
                protected boolean isAccountBelongToCurrentUser(Long accountId) {
                    return true;
                }
            };
        }

        @Bean
        public TransactionService transactionService() {
            return new TransactionServiceImpl();
        }
    }

    @Test
    public void concurrentWithdrawAndRefill() throws InterruptedException {

        long totalSumBefore = accountService.getTotalBankBalance();

        long testAmount = 1000;
        long testAccountId = 1;

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            if (i % 2 == 0) {
                executor.submit(() -> {
                    for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                        executeWithdraw(testAccountId, testAmount);
                    }
                });
            } else {
                executor.submit(() -> {
                    for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                        executeRefill(testAccountId, testAmount);
                    }
                });
            }
        }

        executor.shutdown();

        int timeOfAttempt = 5;
        int attempts = 5;
        boolean again = true;
        while (!executor.awaitTermination(timeOfAttempt, TimeUnit.SECONDS) && again) {
            attempts--;
            again = attempts > 0 ? true : false;
        }
        assertTrue("tasks did not completed!", again);

        long totalSumAfter = accountService.getTotalBankBalance();

        assertEquals("total balance before and after must be equal", totalSumBefore, totalSumAfter);
    }

    @Test
    public void concurrentTransfer() throws InterruptedException {

        long totalSumBefore = accountService.getTotalBankBalance();

        long testAmount = 1000;
        long testFirstAccountId = 1;
        long testSecondAccountId = 2;

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            executor.submit(() -> {
                for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                    executeTransfer(testFirstAccountId, testSecondAccountId, testAmount);
                }
            });
        }

        executor.shutdown();

        int timeOfAttempt = 5;
        int attempts = 5;
        boolean again = true;
        while (!executor.awaitTermination(timeOfAttempt, TimeUnit.SECONDS) && again) {
            attempts--;
            again = attempts > 0 ? true : false;
        }
        assertTrue("tasks did not completed!", again);

        long totalSumAfter = accountService.getTotalBankBalance();

        assertEquals("total balance before and after must be equal", totalSumBefore, totalSumAfter);
    }

    @Test
    public void concurrentWithdrawAndRefillAndTransfer() throws InterruptedException {

        long totalSumBefore = accountService.getTotalBankBalance();

        long testAmount = 1000;
        long testFirstAccountId = 1;
        long testSecondAccountId = 2;

        int TEST_THREADS_NUMBER = 15;

        ExecutorService executor = Executors.newFixedThreadPool(TEST_THREADS_NUMBER);
        for (int i = 0; i < TEST_THREADS_NUMBER; i++) {
            if (i % 3 == 0) {
                executor.submit(() -> {
                    for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                        executeWithdraw(testFirstAccountId, testAmount);
                    }
                });
            } else if (i % 2 == 0) {
                executor.submit(() -> {
                    for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                        executeRefill(testSecondAccountId, testAmount);
                    }
                });
            } else {
                executor.submit(() -> {
                    for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                        executeTransfer(testFirstAccountId, testSecondAccountId, testAmount);
                    }
                });
            }
        }

        executor.shutdown();

        int timeOfAttempt = 5;
        int attempts = 5;
        boolean again = true;
        while (!executor.awaitTermination(timeOfAttempt, TimeUnit.SECONDS) && again) {
            attempts--;
            again = attempts > 0 ? true : false;
        }
        assertTrue("tasks did not completed!", again);

        long totalSumAfter = accountService.getTotalBankBalance();

        assertEquals("total balance before and after must be equal", totalSumBefore, totalSumAfter);
    }

    @Test
    public void concurrentDuplexTransfer() throws InterruptedException {

        long totalSumBefore = accountService.getTotalBankBalance();

        long testAmount = 1000;
        long testFirstAccountId = 1;
        long testSecondAccountId = 2;

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            if (i % 2 == 0) {
                executor.submit(() -> {
                    for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                        executeTransfer(testFirstAccountId, testSecondAccountId, testAmount);
                    }
                });
            } else {
                executor.submit(() -> {
                    for (int y = 0; y < ITERATIONS_NUMBER; y++) {
                        executeTransfer(testSecondAccountId, testFirstAccountId, testAmount);
                    }
                });
            }
        }

        executor.shutdown();

        int timeOfAttempt = 5;
        int attempts = 5;
        boolean again = true;
        while (!executor.awaitTermination(timeOfAttempt, TimeUnit.SECONDS) && again) {
            attempts--;
            again = attempts > 0 ? true : false;
        }
        assertTrue("tasks did not completed!", again);

        long totalSumAfter = accountService.getTotalBankBalance();

        assertEquals("total balance before and after must be equal", totalSumBefore, totalSumAfter);
    }

    private void executeWithdraw(Long accountId, Long amount) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setFromAccountId(accountId);
        withdrawal.setAmount(amount);
        transactionService.withdraw(withdrawal);
    }

    private void executeRefill(Long accountId, Long amount) {
        Refill refill = new Refill();
        refill.setToAccountId(accountId);
        refill.setAmount(amount);
        transactionService.refill(refill);
    }

    private void executeTransfer(Long fromAccountId, Long toAccountId, Long amount) {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(fromAccountId);
        transfer.setToAccountId(toAccountId);
        transfer.setAmount(amount);
        transactionService.transfer(transfer);
    }

}