package br.com.apoia.domain;

import static br.com.apoia.domain.AreaTestSamples.*;
import static br.com.apoia.domain.MentorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.apoia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AreaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Area.class);
        Area area1 = getAreaSample1();
        Area area2 = new Area();
        assertThat(area1).isNotEqualTo(area2);

        area2.setId(area1.getId());
        assertThat(area1).isEqualTo(area2);

        area2 = getAreaSample2();
        assertThat(area1).isNotEqualTo(area2);
    }

    @Test
    void mentorsTest() {
        Area area = getAreaRandomSampleGenerator();
        Mentor mentorBack = getMentorRandomSampleGenerator();

        area.addMentors(mentorBack);
        assertThat(area.getMentors()).containsOnly(mentorBack);

        area.removeMentors(mentorBack);
        assertThat(area.getMentors()).doesNotContain(mentorBack);

        area.mentors(new HashSet<>(Set.of(mentorBack)));
        assertThat(area.getMentors()).containsOnly(mentorBack);

        area.setMentors(new HashSet<>());
        assertThat(area.getMentors()).doesNotContain(mentorBack);
    }
}
