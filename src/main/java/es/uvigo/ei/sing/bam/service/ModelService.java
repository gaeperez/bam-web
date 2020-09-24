package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;
}
