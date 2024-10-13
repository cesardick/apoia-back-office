package br.com.apoia.domain;

import static br.com.apoia.domain.AreaTestSamples.*;
import static br.com.apoia.domain.MentorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.apoia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MentorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mentor.class);
        Mentor mentor1 = getMentorSample1();
        Mentor mentor2 = new Mentor();
        assertThat(mentor1).isNotEqualTo(mentor2);

        mentor2.setId(mentor1.getId());
        assertThat(mentor1).isEqualTo(mentor2);

        mentor2 = getMentorSample2();
        assertThat(mentor1).isNotEqualTo(mentor2);
    }

    @Test
    void areasTest() {
        Mentor mentor = getMentorRandomSampleGenerator();
        Area areaBack = getAreaRandomSampleGenerator();

        mentor.addAreas(areaBack);
        assertThat(mentor.getAreas()).containsOnly(areaBack);
        assertThat(areaBack.getMentors()).containsOnly(mentor);

        mentor.removeAreas(areaBack);
        assertThat(mentor.getAreas()).doesNotContain(areaBack);
        assertThat(areaBack.getMentors()).doesNotContain(mentor);

        mentor.areas(new HashSet<>(Set.of(areaBack)));
        assertThat(mentor.getAreas()).containsOnly(areaBack);
        assertThat(areaBack.getMentors()).containsOnly(mentor);

        mentor.setAreas(new HashSet<>());
        assertThat(mentor.getAreas()).doesNotContain(areaBack);
        assertThat(areaBack.getMentors()).doesNotContain(mentor);
    }
}
