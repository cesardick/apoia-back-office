package br.com.apoia.domain;

import java.util.UUID;

public class MentorTestSamples {

    public static Mentor getMentorSample1() {
        return new Mentor().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1");
    }

    public static Mentor getMentorSample2() {
        return new Mentor().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2");
    }

    public static Mentor getMentorRandomSampleGenerator() {
        return new Mentor().id(UUID.randomUUID()).name(UUID.randomUUID().toString());
    }
}
