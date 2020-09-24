package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.LayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LayerRepository extends CrudRepository<LayerEntity, Integer> {
    Optional<LayerEntity> findByHash(String hash);
}
