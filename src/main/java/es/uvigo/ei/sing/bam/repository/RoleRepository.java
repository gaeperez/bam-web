package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByRole(String role);

    @Override
    Set<RoleEntity> findAll();
}
