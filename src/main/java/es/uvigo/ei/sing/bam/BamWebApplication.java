package es.uvigo.ei.sing.bam;

import es.uvigo.ei.sing.bam.entity.AgentTypeEntity;
import es.uvigo.ei.sing.bam.entity.RoleEntity;
import es.uvigo.ei.sing.bam.entity.ShapeEntity;
import es.uvigo.ei.sing.bam.entity.UserEntity;
import es.uvigo.ei.sing.bam.repository.*;
import es.uvigo.ei.sing.bam.util.Functions;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
@Log4j2
public class BamWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BamWebApplication.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository,
                           AgentRepository agentRepository, EventRepository eventRepository,
                           ShapeRepository shapeRepository, AgentTypeRepository agentTypeRepository,
                           EventTypeRepository eventTypeRepository, LayerRepository layerRepository) {
        return args -> {
            // https://gcanti.github.io/resources/json-schema-to-tcomb/playground/playground.html

            // Create ADMIN role
            RoleEntity roleAdmin = roleRepository.findByRole("ROLE_ADMIN").orElse(new RoleEntity());
            if (roleAdmin.getId() == 0) {
                roleAdmin.setRole("ROLE_ADMIN");
                roleRepository.save(roleAdmin);
            }
            // Create USER role
            RoleEntity roleUser = roleRepository.findByRole("ROLE_USER").orElse(new RoleEntity());
            if (roleUser.getId() == 0) {
                roleUser.setRole("ROLE_USER");
                roleRepository.save(roleUser);
            }

            // Create an admin user
            UserEntity userAdmin = userRepository.findByUsername("admin").orElse(new UserEntity());
            if (userAdmin.getId() == 0) {
                userAdmin.setUsername("admin");
                userAdmin.setPassword("$2a$10$1MXCZOrimNl9XAS..EVl1eA6NQJ9zOyCz4o4B5Aw3nZXlRWT.QBkS");
                userAdmin.setCreated(LocalDateTime.now());
                userAdmin.setEmail("admin@mail.com");
                userAdmin.setFirstname("Gumersindo");
                userAdmin.setSurname("Gonorrhea");
                userAdmin.setRole(roleAdmin);
                userRepository.save(userAdmin);
            }
            // Create a default user
            UserEntity userUser = userRepository.findByUsername("user").orElse(new UserEntity());
            if (userUser.getId() == 0) {
                userUser.setUsername("user");
                userUser.setPassword("$2a$10$1MXCZOrimNl9XAS..EVl1eA6NQJ9zOyCz4o4B5Aw3nZXlRWT.QBkS");
                userUser.setCreated(LocalDateTime.now());
                userUser.setEmail("user@mail.com");
                userUser.setFirstname("Gumersindo");
                userUser.setSurname("Gonorrhea");
                userUser.setRole(roleUser);
                userRepository.save(userUser);
            }

            // Create default agent types
            AgentTypeEntity agentTypeEntity = agentTypeRepository.findByHash("1fb7e521afd4079b7ec7907b01806ac156bc65f38a1bd4cb5612fd6b5aeedf13").orElse(new AgentTypeEntity());
            String type, json, hash;
            if (agentTypeEntity.getId() == 0) {
                type = "Ribosome";
                agentTypeEntity.setType(type);
                agentTypeEntity.setDescription("Testing ribosome");
                json = "{\"type\":\"object\",\"title\":\"Ribosome\",\"description\":\"The ribosome's mRNA.\"," +
                        "\"properties\":{\"mRNA\":{\"type\":\"array\",\"minItems\":1," +
                        "\"items\":{\"type\":\"string\",\"enum\":[\"GCT\",\"GCC\",\"GCA\",\"GCG\"],\"default\":\"GCT\"}}}," +
                        "\"required\":[\"mRNA\"]}";
                agentTypeEntity.setCustomParametersSchema(json);
                hash = type + json;
                agentTypeEntity.setHash(Functions.doHash(hash));
                agentTypeRepository.save(agentTypeEntity);
            }
            agentTypeEntity = agentTypeRepository.findByHash("e13eff2460eaf438e85d1c5ac06719f8e0e1f4bb9377f58c822fbdedacd3edf5").orElse(new AgentTypeEntity());
            if (agentTypeEntity.getId() == 0) {
                type = "tRNA";
                agentTypeEntity.setType(type);
                agentTypeEntity.setDescription("Testing tRNA");
                json = "{\"type\":\"object\",\"title\":\"tRNA\",\"properties\":{\"codon\":{\"type\":\"string\"," +
                        "\"description\":\"The tRNA's codon.\",\"minLength\":3,\"maxLength\":3}},\"required\":[\"codon\"]}";
                agentTypeEntity.setCustomParametersSchema(json);
                hash = type + json;
                agentTypeEntity.setHash(Functions.doHash(hash));
                agentTypeRepository.save(agentTypeEntity);
            }

            // Create default shapes
            ShapeEntity shapeEntity = shapeRepository.findByHash("f50beb5574fd13ce0adc0f2ccb6672aac59c051adcacccf89627cea176c85184").orElse(new ShapeEntity());
            String form;
            if (shapeEntity.getId() == 0) {
                form = "Sphere";
                shapeEntity.setForm(form);
                json = "{\"type\":\"object\",\"title\":\"Sphere\",\"properties\":{\"radius\":{\"type\":\"number\"," +
                        "\"description\":\"The sphere's radius.\",\"minimum\":0}},\"required\":[\"radius\"]}";
                shapeEntity.setDimensionSchema(json);
                hash = form + json;
                shapeEntity.setHash(Functions.doHash(hash));
                shapeRepository.save(shapeEntity);
            }
            shapeEntity = shapeRepository.findByHash("7cabce6301c653484ee284a66b6ffa0897c871b5047c11186f6d89a09a921432").orElse(new ShapeEntity());
            if (shapeEntity.getId() == 0) {
                form = "Capsule";
                shapeEntity.setForm(form);
                json = "{\"type\":\"object\",\"title\":\"Capsule\",\"properties\":{\"radius\":{\"type\":\"number\"," +
                        "\"description\":\"The capsule's radius.\",\"minimum\":0},\"height\":{\"type\":\"number\"," +
                        "\"description\":\"The capsule's height.\",\"minimum\":0}},\"required\":[\"radius\",\"height\"]}";
                shapeEntity.setDimensionSchema(json);
                hash = form + json;
                shapeEntity.setHash(Functions.doHash(hash));
                shapeRepository.save(shapeEntity);
            }
        };
    }
}
