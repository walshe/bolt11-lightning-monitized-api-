package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MonetizedApiInvocation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MonetizedApiInvocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonetizedApiInvocationRepository
    extends JpaRepository<MonetizedApiInvocation, Long>, JpaSpecificationExecutor<MonetizedApiInvocation> {
    @Query(
        "select monetizedApiInvocation from MonetizedApiInvocation monetizedApiInvocation where monetizedApiInvocation.user.login = ?#{principal.username}"
    )
    List<MonetizedApiInvocation> findByUserIsCurrentUser();
}
