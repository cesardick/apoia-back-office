package br.com.apoia.repository;

import br.com.apoia.domain.Area;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AreaRepositoryWithBagRelationships {
    Optional<Area> fetchBagRelationships(Optional<Area> area);

    List<Area> fetchBagRelationships(List<Area> areas);

    Page<Area> fetchBagRelationships(Page<Area> areas);
}
