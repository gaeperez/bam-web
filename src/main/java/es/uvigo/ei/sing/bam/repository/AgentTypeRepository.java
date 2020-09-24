package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.AgentTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AgentTypeRepository extends CrudRepository<AgentTypeEntity, Integer> {
    Optional<AgentTypeEntity> findByHash(String hash);

    @Override
    Set<AgentTypeEntity> findAll();
}
