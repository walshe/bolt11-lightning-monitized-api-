package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Invoice;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    @Query("select invoice from Invoice invoice where invoice.user.login = ?#{principal.username}")
    List<Invoice> findByUserIsCurrentUser();

    @Query("select invoice from Invoice invoice where invoice.user.login = ?#{principal.username} and invoice.settled = false")
    List<Invoice> findByUserIsCurrentUserAndNotSettled();

    @Modifying(clearAutomatically = true)
    @Query("update Invoice i set i.settled = true, i.settledAt = :settledAt where i.id = :id and i.settledAt = null")
    int updateSettledAndSettledAtByIdWhereSettledAtIsNull(@Param(value = "id") long id, @Param(value = "settledAt") LocalDate localDate);
}
