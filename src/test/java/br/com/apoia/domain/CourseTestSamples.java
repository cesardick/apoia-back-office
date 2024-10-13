package br.com.apoia.domain;

import java.util.UUID;

public class CourseTestSamples {

    public static Course getCourseSample1() {
        return new Course().id("id1").title("title1").description("description1");
    }

    public static Course getCourseSample2() {
        return new Course().id("id2").title("title2").description("description2");
    }

    public static Course getCourseRandomSampleGenerator() {
        return new Course().id(UUID.randomUUID().toString()).title(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
