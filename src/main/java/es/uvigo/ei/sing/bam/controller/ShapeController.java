package es.uvigo.ei.sing.bam.controller;

import es.uvigo.ei.sing.bam.controller.form.ShapeForm;
import es.uvigo.ei.sing.bam.entity.ShapeEntity;
import es.uvigo.ei.sing.bam.service.ShapeService;
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
@RequestMapping("/shape")
public class ShapeController {

    @Autowired
    private ShapeService shapeService;

    @GetMapping
    public String list(Model model) {
        Set<ShapeEntity> possibleEntities = shapeService.findAll();

        // Add the value to the view
        model.addAttribute("shapes", possibleEntities);
        return "shape/list";
    }

    @GetMapping(path = {"/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Integer id) throws NullPointerException {
        ShapeEntity possibleShape = shapeService.findById(id);

        ShapeForm shapeForm = new ShapeForm();
        shapeForm.setForm(possibleShape.getForm());
        shapeForm.setDimensionSchema(possibleShape.getDimensionSchema());

        model.addAttribute("shapeForm", shapeForm);
        model.addAttribute("add", false);

        return "shape/add-edit";
    }

    @PostMapping(path = "/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("shapeForm") @Valid ShapeForm shapeForm,
                       BindingResult result, Model model) {
        // Create the hash with the type and the schema
        String hash = Functions.doHash(shapeForm.getForm() + shapeForm.getDimensionSchema());

        // Check if the agent already exists or if the editing agent has the same information
        ShapeEntity possibleEntity = shapeService.findByHash(hash);
        if (possibleEntity == null || possibleEntity.getId() == id) {
            // Validate Schema and retrieve errors
            Functions.validateSchema(shapeForm.getDimensionSchema(), "dimensionSchema", result);

            // Go to the form again if there is an error
            if (result.hasErrors()) {
                model.addAttribute("add", false);
                return "/shape/add-edit";
            } else {
                // Obtain the agent from DB
                ShapeEntity possibleShape = shapeService.findById(id);
                if (possibleShape != null) {
                    possibleShape
                            .setForm(shapeForm.getForm())
                            .setDimensionSchema(shapeForm.getDimensionSchema())
                            .setHash(hash);
                    shapeService.save(possibleShape);
                }
            }
        } else {
            result.rejectValue("form", null, Constants.ALREADY_DEFINED);
            result.rejectValue("dimensionSchema", null, Constants.ALREADY_DEFINED);
            return "/shape/add-edit";
        }

        return "redirect:/shape";
    }

    @GetMapping(path = {"/add"})
    public String add(Model model) throws NullPointerException {
        model.addAttribute("shapeForm", new ShapeForm());
        model.addAttribute("add", true);
        return "shape/add-edit";
    }

    @PostMapping(path = "/add")
    public String add(@ModelAttribute("shapeForm") @Valid ShapeForm shapeForm, BindingResult result, Model model) {
        // Create the hash with the type and the schema
        String hash = Functions.doHash(shapeForm.getForm() + shapeForm.getDimensionSchema());

        // Check if the agent already exists
        ShapeEntity possibleEntity = shapeService.findByHash(hash);

        if (possibleEntity == null) {
            // Validate Schema and retrieve errors
            Functions.validateSchema(shapeForm.getDimensionSchema(), "dimensionSchema", result);

            // Go to the form again if there is an error
            if (result.hasErrors()) {
                model.addAttribute("add", true);
                return "/shape/add-edit";
            } else {
                ShapeEntity shapeDto = new ShapeEntity()
                        .setForm(shapeForm.getForm())
                        .setDimensionSchema(shapeForm.getDimensionSchema())
                        .setHash(hash);
                shapeService.save(shapeDto);
            }
        } else {
            result.rejectValue("form", null, Constants.ALREADY_DEFINED);
            result.rejectValue("dimensionSchema", null, Constants.ALREADY_DEFINED);
            return "/shape/add-edit";
        }

        return "redirect:/shape";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) throws NullPointerException {
        shapeService.deleteById(id);
        return "redirect:/shape";
    }
}
