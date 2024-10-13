package br.com.apoia.service.mapper;

import static br.com.apoia.domain.MentorAsserts.*;
import static br.com.apoia.domain.MentorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MentorMapperTest {

    private MentorMapper mentorMapper;

    @BeforeEach
    void setUp() {
        mentorMapper = new MentorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMentorSample1();
        var actual = mentorMapper.toEntity(mentorMapper.toDto(expected));
        assertMentorAllPropertiesEquals(expected, actual);
    }
}
