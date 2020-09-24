package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Integer> {

}
