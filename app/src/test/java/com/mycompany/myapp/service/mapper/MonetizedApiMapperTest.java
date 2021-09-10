package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonetizedApiMapperTest {

    private MonetizedApiMapper monetizedApiMapper;

    @BeforeEach
    public void setUp() {
        monetizedApiMapper = new MonetizedApiMapperImpl();
    }
}
