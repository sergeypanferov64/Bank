package ru.spanferov.interview.test.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spanferov.interview.test.bank.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
