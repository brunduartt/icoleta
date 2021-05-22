package com.pucminas.icoleta.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectPointMapperTest {

    private CollectPointMapper collectPointMapper;

    @BeforeEach
    public void setUp() {
        collectPointMapper = new CollectPointMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(collectPointMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(collectPointMapper.fromId(null)).isNull();
    }
}
