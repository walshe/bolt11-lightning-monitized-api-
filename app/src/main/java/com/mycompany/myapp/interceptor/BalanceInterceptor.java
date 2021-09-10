package com.mycompany.myapp.interceptor;

import com.mycompany.myapp.domain.Balance;
import com.mycompany.myapp.domain.Invoice;
import com.mycompany.myapp.domain.MonetizedApi;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.BalanceRepository;
import com.mycompany.myapp.repository.InvoiceRepository;
import com.mycompany.myapp.repository.MonetizedApiRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.BalanceService;
import com.mycompany.myapp.web.rest.errors.InsufficientBalanceAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public class BalanceInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(BalanceInterceptor.class);

    private MonetizedApiRepository monetizedApiRepository;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final InvoiceRepository invoiceRepository;
    private final BalanceService balanceService;

    public BalanceInterceptor(MonetizedApiRepository monetizedApiRepository, UserRepository userRepository, BalanceRepository balanceRepository,
                              InvoiceRepository invoiceRepository, BalanceService balanceService) {
        this.monetizedApiRepository = monetizedApiRepository;
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
        this.invoiceRepository = invoiceRepository;
        this.balanceService = balanceService;

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[preHandle][" + request + "]" + "[" + request.getMethod()
            + "]" + request.getRequestURI() + getParameters(request));


        //check if api being access is monetized and if so what does it cost
        Optional<MonetizedApi> monetizedApiOptional = monetizedApiRepository.
            findAll().stream().filter(amonetizedApi -> amonetizedApi.getUri().equals(request.getRequestURI())).findFirst();

        if(!monetizedApiOptional.isPresent()){
            return true;
        }else if (balanceService.isCurrentUserAllowedToInvoke(monetizedApiOptional.get())) {
            return true;
        } else {
            throw new InsufficientBalanceAlertException("User doesn't not have sufficient balance to execute endpoint", "", "");
        }

    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[postHandle][" + request + "]");


        Optional<MonetizedApi> monetizedApiOptional = monetizedApiRepository.
            findAll().stream().filter(amonetizedApi -> amonetizedApi.getUri().equals(request.getRequestURI())).findFirst();

        if(monetizedApiOptional.isPresent()){
            // charge the user for the api invocation
            // record the invocation
            //check if api being access is monetized and if so what does it cost
            balanceService.chargeAndRecordCurrentUser(monetizedApiOptional.get());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            ex.printStackTrace();
        }
        log.info("[afterCompletion][" + request + "][exception: " + ex + "]");
    }

    private String getParameters(HttpServletRequest request) {
        StringBuffer posted = new StringBuffer();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        while (e.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr + "=");
            if (curr.contains("password")
                || curr.contains("pass")
                || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr != null && !ipAddr.equals("")) {
            posted.append("&_psip=" + ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}
