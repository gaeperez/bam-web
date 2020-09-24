package es.uvigo.ei.sing.bam.controller;

import es.uvigo.ei.sing.bam.controller.form.EventTypeForm;
import es.uvigo.ei.sing.bam.entity.EventTypeEntity;
import es.uvigo.ei.sing.bam.service.EventTypeService;
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
@RequestMapping("/event-type")
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    @GetMapping
    public String list(Model model) {
        Set<EventTypeEntity> possibleEntities = eventTypeService.findAll();

        // Add the value to the view
        model.addAttribute("eventTypes", possibleEntities);
        return "event-type/list";
    }

    @GetMapping(path = {"/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Integer id) throws NullPointerException {
        EventTypeEntity possibleEntity = eventTypeService.findById(id);

        EventTypeForm eventTypeForm = new EventTypeForm();
        eventTypeForm.setType(possibleEntity.getType());
        eventTypeForm.setDescription(possibleEntity.getDescription());
        eventTypeForm.setCustomInformationSchema(possibleEntity.getCustomInformationSchema());

        model.addAttribute("eventTypeForm", eventTypeForm);
        model.addAttribute("add", false);

        return "event-type/add-edit";
    }

    @PostMapping(path = "/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("eventTypeForm") @Valid EventTypeForm eventTypeForm,
                       BindingResult result, Model model) {
        // Create the hash with the type and the schema
        String hash = Functions.doHash(eventTypeForm.getType() + eventTypeForm.getCustomInformationSchema());

        // Check if the agent already exists or if the editing agent has the same information
        EventTypeEntity possibleEntity = eventTypeService.findByHash(hash);
        if (possibleEntity == null || possibleEntity.getId() == id) {
            // Validate Schema and retrieve errors
            Functions.validateSchema(eventTypeForm.getCustomInformationSchema(), "customInformationSchema", result);

            // Go to the form again if there is an error
            if (result.hasErrors()) {
                model.addAttribute("add", false);
                return "/event-type/add-edit";
            } else {
                // Obtain the agent from DB
                EventTypeEntity possibleEventTypeEntity = eventTypeService.findById(id);
                if (possibleEventTypeEntity != null) {
                    possibleEventTypeEntity.setType(eventTypeForm.getType())
                            .setDescription(eventTypeForm.getDescription())
                            .setCustomInformationSchema(eventTypeForm.getCustomInformationSchema())
                            .setHash(hash);
                    eventTypeService.save(possibleEventTypeEntity);
                }
            }
        } else {
            result.rejectValue("type", null, Constants.ALREADY_DEFINED);
            result.rejectValue("customInformationSchema", null, Constants.ALREADY_DEFINED);
            return "/event-type/add-edit";
        }

        return "redirect:/event-type";
    }

    @GetMapping(path = {"/add"})
    public String add(Model model) throws NullPointerException {
        model.addAttribute("eventTypeForm", new EventTypeForm());
        model.addAttribute("add", true);
        return "event-type/add-edit";
    }

    @PostMapping(path = "/add")
    public String add(@ModelAttribute("eventTypeForm") @Valid EventTypeForm eventTypeForm, BindingResult result, Model model) {
        // Create the hash with the type and the schema
        String hash = Functions.doHash(eventTypeForm.getType() + eventTypeForm.getCustomInformationSchema());

        // Check if the agent already exists
        EventTypeEntity possibleEntity = eventTypeService.findByHash(hash);
        if (possibleEntity == null) {
            // Validate Schema and retrieve errors
            Functions.validateSchema(eventTypeForm.getCustomInformationSchema(), "customInformationSchema", result);

            // Go to the form again if there is an error
            if (result.hasErrors()) {
                model.addAttribute("add", true);
                return "/event-type/add-edit";
            } else {
                EventTypeEntity eventTypeDto = new EventTypeEntity()
                        .setType(eventTypeForm.getType())
                        .setDescription(eventTypeForm.getDescription())
                        .setCustomInformationSchema(eventTypeForm.getCustomInformationSchema())
                        .setHash(hash);
                eventTypeService.save(eventTypeDto);
            }
        } else {
            result.rejectValue("type", null, Constants.ALREADY_DEFINED);
            result.rejectValue("customInformationSchema", null, Constants.ALREADY_DEFINED);
            return "/event-type/add-edit";
        }

        return "redirect:/event-type";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) throws NullPointerException {
        eventTypeService.deleteById(id);
        return "redirect:/event-type";
    }
}
