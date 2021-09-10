package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonetizedApiInvocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonetizedApiInvocation.class);
        MonetizedApiInvocation monetizedApiInvocation1 = new MonetizedApiInvocation();
        monetizedApiInvocation1.setId(1L);
        MonetizedApiInvocation monetizedApiInvocation2 = new MonetizedApiInvocation();
        monetizedApiInvocation2.setId(monetizedApiInvocation1.getId());
        assertThat(monetizedApiInvocation1).isEqualTo(monetizedApiInvocation2);
        monetizedApiInvocation2.setId(2L);
        assertThat(monetizedApiInvocation1).isNotEqualTo(monetizedApiInvocation2);
        monetizedApiInvocation1.setId(null);
        assertThat(monetizedApiInvocation1).isNotEqualTo(monetizedApiInvocation2);
    }
}
