package br.com.apoia.service.mapper;

import br.com.apoia.domain.Area;
import br.com.apoia.domain.Course;
import br.com.apoia.service.dto.AreaDTO;
import br.com.apoia.service.dto.CourseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {
    @Mapping(target = "area", source = "area", qualifiedByName = "areaId")
    CourseDTO toDto(Course s);

    @Named("areaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AreaDTO toDtoAreaId(Area area);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
