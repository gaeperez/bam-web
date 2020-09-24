package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.entity.LayerEntity;
import es.uvigo.ei.sing.bam.repository.LayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LayerService {

    @Autowired
    private LayerRepository layerRepository;

    public LayerEntity findByHash(String hash) {
        return layerRepository.findByHash(hash).orElse(null);
    }

}
