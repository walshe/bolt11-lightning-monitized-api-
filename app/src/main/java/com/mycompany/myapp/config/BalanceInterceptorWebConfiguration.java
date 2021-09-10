package com.mycompany.myapp.config;

import com.mycompany.myapp.interceptor.BalanceInterceptor;
import com.mycompany.myapp.repository.BalanceRepository;
import com.mycompany.myapp.repository.InvoiceRepository;
import com.mycompany.myapp.repository.MonetizedApiRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.BalanceService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;

import java.util.concurrent.TimeUnit;

@Configuration
//@Profile({ JHipsterConstants.SPRING_PROFILE_PRODUCTION })
public class BalanceInterceptorWebConfiguration implements WebMvcConfigurer {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final MonetizedApiRepository monetizedApiRepository;
    private final InvoiceRepository invoiceRepository;
    private final BalanceService balanceService;

    public BalanceInterceptorWebConfiguration(MonetizedApiRepository monetizedApiRepository, UserRepository userRepository, BalanceRepository balanceRepository, InvoiceRepository invoiceRepository,
                                              BalanceService balanceService) {
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
        this.monetizedApiRepository = monetizedApiRepository;
        this.invoiceRepository = invoiceRepository;
        this.balanceService = balanceService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BalanceInterceptor(monetizedApiRepository, userRepository, balanceRepository, invoiceRepository, balanceService));
    }
}
