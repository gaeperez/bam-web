package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
}
