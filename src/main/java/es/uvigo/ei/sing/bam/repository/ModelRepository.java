package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.ModelEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends CrudRepository<ModelEntity, Integer> {

}
