package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonetizedApiInvocationMapperTest {

    private MonetizedApiInvocationMapper monetizedApiInvocationMapper;

    @BeforeEach
    public void setUp() {
        monetizedApiInvocationMapper = new MonetizedApiInvocationMapperImpl();
    }
}
