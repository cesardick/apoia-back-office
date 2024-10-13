package br.com.apoia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.apoia.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MentorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MentorDTO.class);
        MentorDTO mentorDTO1 = new MentorDTO();
        mentorDTO1.setId(UUID.randomUUID());
        MentorDTO mentorDTO2 = new MentorDTO();
        assertThat(mentorDTO1).isNotEqualTo(mentorDTO2);
        mentorDTO2.setId(mentorDTO1.getId());
        assertThat(mentorDTO1).isEqualTo(mentorDTO2);
        mentorDTO2.setId(UUID.randomUUID());
        assertThat(mentorDTO1).isNotEqualTo(mentorDTO2);
        mentorDTO1.setId(null);
        assertThat(mentorDTO1).isNotEqualTo(mentorDTO2);
    }
}
