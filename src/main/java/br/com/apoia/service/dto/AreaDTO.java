package br.com.apoia.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link br.com.apoia.domain.Area} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AreaDTO implements Serializable {

    private UUID id;

    private String name;

    private Set<MentorDTO> mentors = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MentorDTO> getMentors() {
        return mentors;
    }

    public void setMentors(Set<MentorDTO> mentors) {
        this.mentors = mentors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaDTO)) {
            return false;
        }

        AreaDTO areaDTO = (AreaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, areaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", mentors=" + getMentors() +
            "}";
    }
}
