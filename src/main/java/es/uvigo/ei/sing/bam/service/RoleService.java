package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.entity.RoleEntity;
import es.uvigo.ei.sing.bam.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleEntity findByrole(String role) {
        return roleRepository.findByRole(role).orElse(null);
    }

    public Set<RoleEntity> findAll() {
        return roleRepository.findAll();
    }
}
