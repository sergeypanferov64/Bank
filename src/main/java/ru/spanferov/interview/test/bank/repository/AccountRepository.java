package ru.spanferov.interview.test.bank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.spanferov.interview.test.bank.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a JOIN a.user u WHERE a.accountId=:accountId AND u.username=:username")
    Account findOneByAccountIdAndUserName(@Param("accountId") Long accountId, @Param("username") String username);

    @Query("SELECT a FROM Account a JOIN a.user u WHERE u.username=:username")
    Page<Account> pageByUserName(@Param("username") String username, Pageable pageable);
}
