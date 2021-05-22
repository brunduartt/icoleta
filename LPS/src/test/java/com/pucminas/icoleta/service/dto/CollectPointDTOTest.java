package com.pucminas.icoleta.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pucminas.icoleta.web.rest.TestUtil;

public class CollectPointDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectPointDTO.class);
        CollectPointDTO collectPointDTO1 = new CollectPointDTO();
        collectPointDTO1.setId(1L);
        CollectPointDTO collectPointDTO2 = new CollectPointDTO();
        assertThat(collectPointDTO1).isNotEqualTo(collectPointDTO2);
        collectPointDTO2.setId(collectPointDTO1.getId());
        assertThat(collectPointDTO1).isEqualTo(collectPointDTO2);
        collectPointDTO2.setId(2L);
        assertThat(collectPointDTO1).isNotEqualTo(collectPointDTO2);
        collectPointDTO1.setId(null);
        assertThat(collectPointDTO1).isNotEqualTo(collectPointDTO2);
    }
}
