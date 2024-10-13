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
 * Mapper for the entity {@link Mentor} and its DTO {@link MentorDTO}.
 */
@Mapper(componentModel = "spring")
public interface MentorMapper extends EntityMapper<MentorDTO, Mentor> {
    @Mapping(target = "areas", source = "areas", qualifiedByName = "areaIdSet")
    MentorDTO toDto(Mentor s);

    @Mapping(target = "areas", ignore = true)
    @Mapping(target = "removeAreas", ignore = true)
    Mentor toEntity(MentorDTO mentorDTO);

    @Named("areaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AreaDTO toDtoAreaId(Area area);

    @Named("areaIdSet")
    default Set<AreaDTO> toDtoAreaIdSet(Set<Area> area) {
        return area.stream().map(this::toDtoAreaId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
