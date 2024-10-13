package br.com.apoia.repository;

import br.com.apoia.domain.Area;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AreaRepositoryWithBagRelationshipsImpl implements AreaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String AREAS_PARAMETER = "areas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Area> fetchBagRelationships(Optional<Area> area) {
        return area.map(this::fetchMentors);
    }

    @Override
    public Page<Area> fetchBagRelationships(Page<Area> areas) {
        return new PageImpl<>(fetchBagRelationships(areas.getContent()), areas.getPageable(), areas.getTotalElements());
    }

    @Override
    public List<Area> fetchBagRelationships(List<Area> areas) {
        return Optional.of(areas).map(this::fetchMentors).orElse(Collections.emptyList());
    }

    Area fetchMentors(Area result) {
        return entityManager
            .createQuery("select area from Area area left join fetch area.mentors where area.id = :id", Area.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Area> fetchMentors(List<Area> areas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, areas.size()).forEach(index -> order.put(areas.get(index).getId(), index));
        List<Area> result = entityManager
            .createQuery("select area from Area area left join fetch area.mentors where area in :areas", Area.class)
            .setParameter(AREAS_PARAMETER, areas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
