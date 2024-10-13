package br.com.apoia.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link br.com.apoia.domain.Mentor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MentorDTO implements Serializable {

    private UUID id;

    private String name;

    private Set<AreaDTO> areas = new HashSet<>();

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

    public Set<AreaDTO> getAreas() {
        return areas;
    }

    public void setAreas(Set<AreaDTO> areas) {
        this.areas = areas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MentorDTO)) {
            return false;
        }

        MentorDTO mentorDTO = (MentorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mentorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MentorDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", areas=" + getAreas() +
            "}";
    }
}
