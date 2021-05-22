package com.pucminas.icoleta.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pucminas.icoleta.web.rest.TestUtil;

public class CollectPointTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectPoint.class);
        CollectPoint collectPoint1 = new CollectPoint();
        collectPoint1.setId(1L);
        CollectPoint collectPoint2 = new CollectPoint();
        collectPoint2.setId(collectPoint1.getId());
        assertThat(collectPoint1).isEqualTo(collectPoint2);
        collectPoint2.setId(2L);
        assertThat(collectPoint1).isNotEqualTo(collectPoint2);
        collectPoint1.setId(null);
        assertThat(collectPoint1).isNotEqualTo(collectPoint2);
    }
}
