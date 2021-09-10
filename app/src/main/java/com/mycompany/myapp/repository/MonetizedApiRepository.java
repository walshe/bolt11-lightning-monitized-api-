package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MonetizedApi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MonetizedApi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonetizedApiRepository extends JpaRepository<MonetizedApi, Long>, JpaSpecificationExecutor<MonetizedApi> {}
