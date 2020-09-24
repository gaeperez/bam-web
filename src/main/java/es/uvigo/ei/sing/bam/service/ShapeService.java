package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.entity.ShapeEntity;
import es.uvigo.ei.sing.bam.repository.ShapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ShapeService {

    @Autowired
    private ShapeRepository shapeRepository;

    public Set<ShapeEntity> findAll() {
        return shapeRepository.findAll();
    }

    public ShapeEntity findById(Integer id) {
        return shapeRepository.findById(id).orElse(null);
    }

    public ShapeEntity findByHash(String hash) {
        return shapeRepository.findByHash(hash).orElse(null);
    }

    public void delete(ShapeEntity shapeEntity) {
        shapeRepository.delete(shapeEntity);
    }

    public void deleteById(Integer id) {
        shapeRepository.deleteById(id);
    }

    public ShapeEntity save(ShapeEntity shapeEntity) {
        return shapeRepository.save(shapeEntity);
    }
}
