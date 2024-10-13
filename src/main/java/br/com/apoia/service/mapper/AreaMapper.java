package br.com.apoia.service.mapper;

import br.com.apoia.domain.Area;
import br.com.apoia.domain.Mentor;
import br.com.apoia.service.dto.AreaDTO;
import br.com.apoia.service.dto.MentorDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Area} and its DTO {@link AreaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AreaMapper extends EntityMapper<AreaDTO, Area> {
    @Mapping(target = "mentors", source = "mentors", qualifiedByName = "mentorIdSet")
    AreaDTO toDto(Area s);

    @Mapping(target = "removeMentors", ignore = true)
    Area toEntity(AreaDTO areaDTO);

    @Named("mentorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MentorDTO toDtoMentorId(Mentor mentor);

    @Named("mentorIdSet")
    default Set<MentorDTO> toDtoMentorIdSet(Set<Mentor> mentor) {
        return mentor.stream().map(this::toDtoMentorId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
