package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.entity.AgentEntity;
import es.uvigo.ei.sing.bam.entity.UserEntity;
import es.uvigo.ei.sing.bam.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    public Set<AgentEntity> findAll() {
        return agentRepository.findAll();
    }

    public Set<AgentEntity> findByUser(UserEntity userEntity) {
        return agentRepository.findByUser(userEntity);
    }

    public AgentEntity findById(Integer id) {
        return agentRepository.findById(id).orElse(null);
    }

    public AgentEntity findByHash(String hash) {
        return agentRepository.findByHash(hash).orElse(null);
    }

    public void delete(AgentEntity agentEntity) {
        agentRepository.delete(agentEntity);
    }

    public void deleteById(Integer id) {
        agentRepository.deleteById(id);
    }

    public AgentEntity save(AgentEntity agentEntity) {
        return agentRepository.save(agentEntity);
    }
}
