package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.BalanceRepository;
import com.mycompany.myapp.repository.InvoiceRepository;
import com.mycompany.myapp.repository.MonetizedApiInvocationRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.BalanceDTO;
import com.mycompany.myapp.service.mapper.BalanceMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mycompany.myapp.web.rest.errors.InsufficientBalanceAlertException;
import liquibase.pro.packaged.B;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Balance}.
 */
@Service
@Transactional
public class BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceService.class);

    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final ImperviousLightningService imperviousLightningService;
    private final MonetizedApiInvocationRepository monetizedApiInvocationRepository;

    private final BalanceMapper balanceMapper;

    public BalanceService(BalanceRepository balanceRepository, BalanceMapper balanceMapper, UserRepository userRepository,
                          InvoiceRepository invoiceRepository, ImperviousLightningService imperviousLightningService,
                          MonetizedApiInvocationRepository monetizedApiInvocationRepository) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.imperviousLightningService = imperviousLightningService;
        this.monetizedApiInvocationRepository = monetizedApiInvocationRepository;
    }

    public boolean isCurrentUserAllowedToInvoke(MonetizedApi monetizedApi) {
        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        log.info("User is " + userLogin);
        User existingUser = userRepository.findOneByLogin(userLogin).orElseThrow();

        // check user roles
        //if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN) && SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER) && monetizedApi.getPriceInSats() > 0) {
            Balance balance = balanceRepository.findOneByUser(existingUser);
            if (balance == null || balance.getSats() < monetizedApi.getPriceInSats()) {

                //before we reject lets see if any outstanding invoices for user need settling and then check balance again

                List<Invoice> unsettledInvoices = invoiceRepository.findByUserIsCurrentUserAndNotSettled();
                if (unsettledInvoices.isEmpty()) {
                    return false;
                }

                unsettledInvoices.forEach(unsettledInvoice -> {

                    //call impervious and find out if newly settled
                    //if settled then set invoice.settled = true, pubkey etc where settledTimestamp = null (this fixes optimistic concurrency issue)
                    if(imperviousLightningService.isBoltinvoicePaid( unsettledInvoice.getBoltInvoice())){
                        invoiceRepository.updateSettledAndSettledAtByIdWhereSettledAtIsNull(unsettledInvoice.getId(), LocalDate.now());
                         // increment users balance in balance table
                        balanceRepository.incrementSats(existingUser, unsettledInvoice.getSats());

                    }

                });

                balance = balanceRepository.findOneByUser(existingUser);


                if (balance == null || balance.getSats() < monetizedApi.getPriceInSats()) {
                    return false;
                } else {
                    return true;
                }


            }else{
                return true;
            }

        } else {
            return true;
        }
    }

    public void chargeAndRecordCurrentUser(MonetizedApi monetizedApi){
        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        log.info("User is " + userLogin);
        User existingUser = userRepository.findOneByLogin(userLogin).orElseThrow();

        // check user roles
        //if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN) && SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)) {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER) && monetizedApi.getPriceInSats() > 0) {
            balanceRepository.decrementSats(existingUser, monetizedApi.getPriceInSats());
            monetizedApiInvocationRepository.save(new MonetizedApiInvocation().user(existingUser).uri(monetizedApi.getUri()).createdAt(LocalDate.now()));
        }
    }

    /**
     * Save a balance.
     *
     * @param balanceDTO the entity to save.
     * @return the persisted entity.
     */
    public BalanceDTO save(BalanceDTO balanceDTO) {
        log.debug("Request to save Balance : {}", balanceDTO);
        Balance balance = balanceMapper.toEntity(balanceDTO);
        balance = balanceRepository.save(balance);
        return balanceMapper.toDto(balance);
    }

    /**
     * Partially update a balance.
     *
     * @param balanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BalanceDTO> partialUpdate(BalanceDTO balanceDTO) {
        log.debug("Request to partially update Balance : {}", balanceDTO);

        return balanceRepository
            .findById(balanceDTO.getId())
            .map(
                existingBalance -> {
                    balanceMapper.partialUpdate(existingBalance, balanceDTO);

                    return existingBalance;
                }
            )
            .map(balanceRepository::save)
            .map(balanceMapper::toDto);
    }

    /**
     * Get all the balances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BalanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Balances");
        return balanceRepository.findAll(pageable).map(balanceMapper::toDto);
    }

    /**
     * Get one balance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BalanceDTO> findOne(Long id) {
        log.debug("Request to get Balance : {}", id);
        return balanceRepository.findById(id).map(balanceMapper::toDto);
    }

    public BalanceDTO findByCurrentUser() {
        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        log.info("User is " + userLogin);
        User user = userRepository.findOneByLogin(userLogin).orElseThrow();
        Balance balance = balanceRepository.findOneByUser(user);
        if(balance == null){
            //create a default balance
            Balance balanceToSave = new Balance();
            balanceToSave.sats(0L).user(user).updatedAt(LocalDate.now());
            balance = balanceRepository.save(balanceToSave);
        }
        BalanceDTO dto = balanceMapper.toDto(balance);

        return dto;
    }

    /**
     * Delete the balance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Balance : {}", id);
        balanceRepository.deleteById(id);
    }
}
