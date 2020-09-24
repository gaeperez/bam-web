package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.ShapeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ShapeRepository extends CrudRepository<ShapeEntity, Integer> {
    Optional<ShapeEntity> findByHash(String hash);

    @Override
    Set<ShapeEntity> findAll();
}
