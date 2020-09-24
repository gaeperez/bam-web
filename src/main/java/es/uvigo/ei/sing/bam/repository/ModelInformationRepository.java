package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.ModelInformationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelInformationRepository extends CrudRepository<ModelInformationEntity, Integer> {

}
