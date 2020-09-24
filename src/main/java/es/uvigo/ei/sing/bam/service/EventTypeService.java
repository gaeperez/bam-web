package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.entity.EventTypeEntity;
import es.uvigo.ei.sing.bam.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EventTypeService {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    public Set<EventTypeEntity> findAll() {
        return eventTypeRepository.findAll();
    }

    public EventTypeEntity findById(Integer id) {
        return eventTypeRepository.findById(id).orElse(null);
    }

    public EventTypeEntity findByHash(String hash) {
        return eventTypeRepository.findByHash(hash).orElse(null);
    }

    public void delete(EventTypeEntity eventTypeEntity) {
        eventTypeRepository.delete(eventTypeEntity);
    }

    public void deleteById(Integer id) {
        eventTypeRepository.deleteById(id);
    }

    public EventTypeEntity save(EventTypeEntity eventTypeEntity) {
        return eventTypeRepository.save(eventTypeEntity);
    }
}
