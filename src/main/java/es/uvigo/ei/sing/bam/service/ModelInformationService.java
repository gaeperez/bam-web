package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.repository.ModelInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelInformationService {

    @Autowired
    private ModelInformationRepository modelInformationRepository;
}
