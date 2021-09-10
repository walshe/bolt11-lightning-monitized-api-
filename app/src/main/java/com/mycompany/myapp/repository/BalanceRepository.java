package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Balance;
import com.mycompany.myapp.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Balance entity.
 */
@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long>, JpaSpecificationExecutor<Balance> {

    Balance findOneByUser(User user);

    @Modifying(clearAutomatically = true)
    @Query("update Balance b set b.sats = b.sats + :sats where b.user = :user")
    int incrementSats(@Param(value = "user") User user, @Param(value = "sats") Long sats);

    @Modifying(clearAutomatically = true)
    @Query("update Balance b set b.sats = b.sats - :sats where b.user = :user")
    int decrementSats(@Param(value = "user") User user, @Param(value = "sats") Long sats);
}
