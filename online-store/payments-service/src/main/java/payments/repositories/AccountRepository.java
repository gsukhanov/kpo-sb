package payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import payments.domains.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.userId = :userId")
    int addBalance(@Param("userId") Integer userId, @Param("amount") long amount);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.userId = :userId AND a.balance >= :amount")
    int subtractIfEnough(@Param("userId") Integer userId, @Param("amount") long amount);
}
