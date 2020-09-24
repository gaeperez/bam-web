package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.AgentEntity;
import es.uvigo.ei.sing.bam.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AgentRepository extends CrudRepository<AgentEntity, Integer> {
    Optional<AgentEntity> findByHash(String hash);

    @Override
    Set<AgentEntity> findAll();

    Set<AgentEntity> findByUser(UserEntity user);
}
