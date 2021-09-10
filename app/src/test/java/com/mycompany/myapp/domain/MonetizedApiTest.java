package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonetizedApiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonetizedApi.class);
        MonetizedApi monetizedApi1 = new MonetizedApi();
        monetizedApi1.setId(1L);
        MonetizedApi monetizedApi2 = new MonetizedApi();
        monetizedApi2.setId(monetizedApi1.getId());
        assertThat(monetizedApi1).isEqualTo(monetizedApi2);
        monetizedApi2.setId(2L);
        assertThat(monetizedApi1).isNotEqualTo(monetizedApi2);
        monetizedApi1.setId(null);
        assertThat(monetizedApi1).isNotEqualTo(monetizedApi2);
    }
}
