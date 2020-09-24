package es.uvigo.ei.sing.bam.repository;

import es.uvigo.ei.sing.bam.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Override
    Set<UserEntity> findAll();
}
