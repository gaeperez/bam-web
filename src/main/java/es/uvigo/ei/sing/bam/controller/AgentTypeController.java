package es.uvigo.ei.sing.bam.controller;

import es.uvigo.ei.sing.bam.controller.form.AgentTypeForm;
import es.uvigo.ei.sing.bam.entity.AgentTypeEntity;
import es.uvigo.ei.sing.bam.service.AgentTypeService;
import es.uvigo.ei.sing.bam.util.Constants;
import es.uvigo.ei.sing.bam.util.Functions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Log4j2
@Controller
@RequestMapping("/agent-type")
public class AgentTypeController {

    @Autowired
    private AgentTypeService agentTypeService;

    @GetMapping
    public String list(Model model) {
        Set<AgentTypeEntity> possibleEntities = agentTypeService.findAll();

        // Add the value to the view
        model.addAttribute("agentTypes", possibleEntities);
        return "agent-type/list";
    }

    @GetMapping(path = {"/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Integer id) throws NullPointerException {
        AgentTypeEntity possibleAgentType = agentTypeService.findById(id);

        AgentTypeForm agentTypeForm = new AgentTypeForm();
        agentTypeForm.setType(possibleAgentType.getType());
        agentTypeForm.setDescription(possibleAgentType.getDescription());
        agentTypeForm.setCustomParametersSchema(possibleAgentType.getCustomParametersSchema());

        model.addAttribute("agentTypeForm", agentTypeForm);

        return "agent-type/add-edit";
    }

    @PostMapping(path = "/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("agentTypeForm") @Valid AgentTypeForm agentTypeForm,
                       BindingResult result, Model model) {
        // Create the hash with the type and the schema
        String hash = Functions.doHash(agentTypeForm.getType() + agentTypeForm.getCustomParametersSchema());

        // Check if the agent already exists or if the editing agent has the same information
        AgentTypeEntity possibleEntity = agentTypeService.findByHash(hash);
        if (possibleEntity == null || possibleEntity.getId() == id) {
            // Validate Schema and retrieve errors
            Functions.validateSchema(agentTypeForm.getCustomParametersSchema(), "customParametersSchema", result);

            // Go to the form again if there is an error
            if (result.hasErrors()) {
                model.addAttribute("add", false);
                return "/agent-type/add-edit";
            } else {
                // Obtain the agent from DB
                AgentTypeEntity possibleAgentType = agentTypeService.findById(id);
                if (possibleAgentType != null) {
                    possibleAgentType
                            .setType(agentTypeForm.getType())
                            .setDescription(agentTypeForm.getDescription())
                            .setCustomParametersSchema(agentTypeForm.getCustomParametersSchema())
                            .setHash(hash);
                    agentTypeService.save(possibleAgentType);
                }
            }
        } else {
            result.rejectValue("type", null, Constants.ALREADY_DEFINED);
            result.rejectValue("customParametersSchema", null, Constants.ALREADY_DEFINED);
            return "/agent-type/add-edit";
        }

        return "redirect:/agent-type";
    }

    @GetMapping(path = {"/add"})
    public String add(Model model) throws NullPointerException {
        model.addAttribute("agentTypeForm", new AgentTypeForm());
        model.addAttribute("add", true);
        return "agent-type/add-edit";
    }

    @PostMapping(path = "/add")
    public String add(@ModelAttribute("agentTypeForm") @Valid AgentTypeForm agentTypeForm, BindingResult result, Model model) {
        // Create the hash with the type and the schema
        String hash = Functions.doHash(agentTypeForm.getType() + agentTypeForm.getCustomParametersSchema());

        // Check if the agent already exists
        AgentTypeEntity possibleEntity = agentTypeService.findByHash(hash);

        if (possibleEntity == null) {
            // Validate Schema and retrieve errors
            Functions.validateSchema(agentTypeForm.getCustomParametersSchema(), "customParametersSchema", result);

            // Go to the form again if there is an error
            if (result.hasErrors()) {
                model.addAttribute("add", true);
                return "/agent-type/add-edit";
            } else {
                AgentTypeEntity agentTypeEntity = new AgentTypeEntity()
                        .setType(agentTypeForm.getType())
                        .setDescription(agentTypeForm.getDescription())
                        .setCustomParametersSchema(agentTypeForm.getCustomParametersSchema())
                        .setHash(hash);
                agentTypeService.save(agentTypeEntity);
            }
        } else {
            result.rejectValue("type", null, Constants.ALREADY_DEFINED);
            result.rejectValue("customParametersSchema", null, Constants.ALREADY_DEFINED);
            return "/agent-type/add-edit";
        }

        return "redirect:/agent-type";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) throws NullPointerException {
        agentTypeService.deleteById(id);
        return "redirect:/agent-type";
    }
}
