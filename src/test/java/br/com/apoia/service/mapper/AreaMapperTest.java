package br.com.apoia.service.mapper;

import static br.com.apoia.domain.AreaAsserts.*;
import static br.com.apoia.domain.AreaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AreaMapperTest {

    private AreaMapper areaMapper;

    @BeforeEach
    void setUp() {
        areaMapper = new AreaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAreaSample1();
        var actual = areaMapper.toEntity(areaMapper.toDto(expected));
        assertAreaAllPropertiesEquals(expected, actual);
    }
}
