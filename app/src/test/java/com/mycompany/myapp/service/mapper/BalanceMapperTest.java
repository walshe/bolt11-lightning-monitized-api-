package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BalanceMapperTest {

    private BalanceMapper balanceMapper;

    @BeforeEach
    public void setUp() {
        balanceMapper = new BalanceMapperImpl();
    }
}
