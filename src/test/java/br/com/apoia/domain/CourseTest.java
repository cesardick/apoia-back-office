package br.com.apoia.domain;

import static br.com.apoia.domain.AreaTestSamples.*;
import static br.com.apoia.domain.CourseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.apoia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = getCourseSample1();
        Course course2 = new Course();
        assertThat(course1).isNotEqualTo(course2);

        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);

        course2 = getCourseSample2();
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    void areaTest() {
        Course course = getCourseRandomSampleGenerator();
        Area areaBack = getAreaRandomSampleGenerator();

        course.setArea(areaBack);
        assertThat(course.getArea()).isEqualTo(areaBack);

        course.area(null);
        assertThat(course.getArea()).isNull();
    }
}
