package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.EventTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EventTypeRepository extends CrudRepository<EventTypeEntity, Integer> {
    Optional<EventTypeEntity> findByHash(String hash);

    @Override
    Set<EventTypeEntity> findAll();
}
