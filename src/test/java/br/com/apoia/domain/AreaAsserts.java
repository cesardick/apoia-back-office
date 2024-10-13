package br.com.apoia.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AreaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAreaAllPropertiesEquals(Area expected, Area actual) {
        assertAreaAutoGeneratedPropertiesEquals(expected, actual);
        assertAreaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAreaAllUpdatablePropertiesEquals(Area expected, Area actual) {
        assertAreaUpdatableFieldsEquals(expected, actual);
        assertAreaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAreaAutoGeneratedPropertiesEquals(Area expected, Area actual) {
        assertThat(expected)
            .as("Verify Area auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAreaUpdatableFieldsEquals(Area expected, Area actual) {
        assertThat(expected)
            .as("Verify Area relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAreaUpdatableRelationshipsEquals(Area expected, Area actual) {
        assertThat(expected)
            .as("Verify Area relationships")
            .satisfies(e -> assertThat(e.getMentors()).as("check mentors").isEqualTo(actual.getMentors()));
    }
}
