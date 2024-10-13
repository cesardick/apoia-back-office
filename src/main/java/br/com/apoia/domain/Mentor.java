package br.com.apoia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A Mentor.
 */
@Entity
@Table(name = "mentor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mentor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "mentors")
    @JsonIgnoreProperties(value = { "mentors" }, allowSetters = true)
    private Set<Area> areas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Mentor id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Mentor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Area> getAreas() {
        return this.areas;
    }

    public void setAreas(Set<Area> areas) {
        if (this.areas != null) {
            this.areas.forEach(i -> i.removeMentors(this));
        }
        if (areas != null) {
            areas.forEach(i -> i.addMentors(this));
        }
        this.areas = areas;
    }

    public Mentor areas(Set<Area> areas) {
        this.setAreas(areas);
        return this;
    }

    public Mentor addAreas(Area area) {
        this.areas.add(area);
        area.getMentors().add(this);
        return this;
    }

    public Mentor removeAreas(Area area) {
        this.areas.remove(area);
        area.getMentors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mentor)) {
            return false;
        }
        return getId() != null && getId().equals(((Mentor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mentor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
