package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonetizedApiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonetizedApiDTO.class);
        MonetizedApiDTO monetizedApiDTO1 = new MonetizedApiDTO();
        monetizedApiDTO1.setId(1L);
        MonetizedApiDTO monetizedApiDTO2 = new MonetizedApiDTO();
        assertThat(monetizedApiDTO1).isNotEqualTo(monetizedApiDTO2);
        monetizedApiDTO2.setId(monetizedApiDTO1.getId());
        assertThat(monetizedApiDTO1).isEqualTo(monetizedApiDTO2);
        monetizedApiDTO2.setId(2L);
        assertThat(monetizedApiDTO1).isNotEqualTo(monetizedApiDTO2);
        monetizedApiDTO1.setId(null);
        assertThat(monetizedApiDTO1).isNotEqualTo(monetizedApiDTO2);
    }
}
