package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.entity.AgentTypeEntity;
import es.uvigo.ei.sing.bam.repository.AgentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AgentTypeService {

    @Autowired
    private AgentTypeRepository agentTypeRepository;

    public Set<AgentTypeEntity> findAll() {
        return agentTypeRepository.findAll();
    }

    public AgentTypeEntity findById(Integer id) {
        return agentTypeRepository.findById(id).orElse(null);
    }

    public AgentTypeEntity findByHash(String hash) {
        return agentTypeRepository.findByHash(hash).orElse(null);
    }

    public void delete(AgentTypeEntity agentTypeEntity) {
        agentTypeRepository.delete(agentTypeEntity);
    }

    public void deleteById(Integer id) {
        agentTypeRepository.deleteById(id);
    }

    public AgentTypeEntity save(AgentTypeEntity agentTypeEntity) {
        return agentTypeRepository.save(agentTypeEntity);
    }
}
