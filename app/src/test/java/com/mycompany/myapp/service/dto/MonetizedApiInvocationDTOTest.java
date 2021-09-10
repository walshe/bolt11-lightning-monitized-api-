package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonetizedApiInvocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonetizedApiInvocationDTO.class);
        MonetizedApiInvocationDTO monetizedApiInvocationDTO1 = new MonetizedApiInvocationDTO();
        monetizedApiInvocationDTO1.setId(1L);
        MonetizedApiInvocationDTO monetizedApiInvocationDTO2 = new MonetizedApiInvocationDTO();
        assertThat(monetizedApiInvocationDTO1).isNotEqualTo(monetizedApiInvocationDTO2);
        monetizedApiInvocationDTO2.setId(monetizedApiInvocationDTO1.getId());
        assertThat(monetizedApiInvocationDTO1).isEqualTo(monetizedApiInvocationDTO2);
        monetizedApiInvocationDTO2.setId(2L);
        assertThat(monetizedApiInvocationDTO1).isNotEqualTo(monetizedApiInvocationDTO2);
        monetizedApiInvocationDTO1.setId(null);
        assertThat(monetizedApiInvocationDTO1).isNotEqualTo(monetizedApiInvocationDTO2);
    }
}
