package es.uvigo.ei.sing.bam.controller;

import es.uvigo.ei.sing.bam.controller.form.AgentForm;
import es.uvigo.ei.sing.bam.controller.form.LayerForm;
import es.uvigo.ei.sing.bam.entity.*;
import es.uvigo.ei.sing.bam.exception.ForbiddenException;
import es.uvigo.ei.sing.bam.service.*;
import es.uvigo.ei.sing.bam.util.Constants;
import es.uvigo.ei.sing.bam.util.Functions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/agent")
public class AgentController {

    // TODO: 28/04/2020 duplicate method to copy another user agent

    @Autowired
    private AgentService agentService;
    @Autowired
    private AgentTypeService agentTypeService;
    @Autowired
    private ShapeService shapeService;
    @Autowired
    private UserService userService;
    @Autowired
    private LayerService layerService;

    @GetMapping
    public String list(Model model) {
        // Get auth user
        UserEntity authUser = getAuthenticatedUser();

        // If user is admin, show all agents. Otherwise show only user agents
        Set<AgentEntity> possibleEntities;
        if (authUser.getRole().getRole().equalsIgnoreCase(Constants.ROLE_ADMIN))
            possibleEntities = agentService.findAll();
        else
            possibleEntities = agentService.findByUser(authUser);

        // Add the value to the view
        model.addAttribute("agents", possibleEntities);
        return "agent/list";
    }

    @GetMapping(path = {"/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Integer id) {
        // Get auth user
        UserEntity authUser = getAuthenticatedUser();

        // Get the info for the current agent
        AgentEntity possibleEntity = agentService.findById(id);

        // Admin can edit everything. Regular users can only edit their agents
        if (canAccessResource(authUser, possibleEntity)) {
            // Convert the layers to their corresponding form (~DTO)
            List<LayerForm> layerForms = new ArrayList<>();
            Set<LayerEntity> layerEntities = possibleEntity.getLayers();
            for (LayerEntity layerEntity : layerEntities) {
                LayerForm layerForm = new LayerForm();
                layerForm.setName(layerEntity.getName());
                layerForm.setColor(layerEntity.getColor());
                layerForm.setDimensionShape(layerEntity.getDimensions());
                layerForm.setSelectedShape(layerEntity.getShape().getId());
                layerForm.setHash(layerEntity.getHash());

                layerForms.add(layerForm);
            }

            // Fill the form with the info of the retrieved agent
            AgentForm agentForm = new AgentForm();
            agentForm.setName(possibleEntity.getName());
            agentForm.setMolecularWeight(possibleEntity.getMolecularWeight());
            agentForm.setDiffusionRate(possibleEntity.getDiffusionRate());
            agentForm.setColor(possibleEntity.getColor());
            agentForm.setSelectedType(possibleEntity.getType().getId());
            agentForm.setCustomParametersType(possibleEntity.getCustomParameters());
            agentForm.setSelectedShape(possibleEntity.getShape().getId());
            agentForm.setDimensionShape(possibleEntity.getDimensions());
            agentForm.setLayers(layerForms);
            agentForm.setMapIdShapeEntity(getAllShapes());
            agentForm.setMapIdTypeEntity(getAllTypes());

            // Pass the variables to the model
            model.addAttribute("agentForm", agentForm);
            model.addAttribute("add", false);

            return "agent/add-edit";
        } else
            throw new ForbiddenException("The user is trying to access a forbidden resource!");
    }

    @PostMapping(path = "/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("agentForm") @Valid AgentForm agentForm,
                       BindingResult result, Model model) {
        // Get auth user
        UserEntity authUser = this.getAuthenticatedUser();

        // Get the info for the current agent
        AgentEntity possibleEntityById = agentService.findById(id);

        // Admin can edit everything. Regular users can only edit their agents
        if (canAccessResource(authUser, possibleEntityById)) {
            // Set form variables
            Map<Integer, ShapeEntity> allShapes = this.getAllShapes();
            Map<Integer, AgentTypeEntity> allTypes = this.getAllTypes();
            agentForm.setMapIdShapeEntity(allShapes);
            agentForm.setMapIdTypeEntity(allTypes);

            // Create the hash with the type and the schema
            String hash = Functions.doHash(agentForm.getName() + agentForm.getMolecularWeight()
                    + agentForm.getDiffusionRate() + agentForm.getCustomParametersType() + agentForm.getDimensionShape());

            // Check if there is another agent with the same information or if the agent is the same
            AgentEntity possibleEntityByHash = agentService.findByHash(hash);
            if (possibleEntityByHash == null || possibleEntityByHash.getId() == id) {
                // Check if the generated jsons are valid against their schemas
                this.validateJSONs(agentForm, result, allShapes, allTypes);

                // Go to the form again if there is an error
                if (result.hasErrors()) {
                    model.addAttribute("add", false);
                    return "/agent/add-edit";
                } else {
                    // Get agent type and shape dto to be included in the agent
                    AgentTypeEntity agentTypeEntity = agentTypeService.findById(agentForm.getSelectedType());
                    ShapeEntity shapeEntity = shapeService.findById(agentForm.getSelectedShape());

                    // Obtain the agent from DB
                    if (possibleEntityById != null) {
                        // Add the rest of the fields
                        possibleEntityById
                                .setName(agentForm.getName())
                                .setMolecularWeight(agentForm.getMolecularWeight())
                                .setDiffusionRate(agentForm.getDiffusionRate())
                                .setColor(agentForm.getColor())
                                .setCustomParameters(agentForm.getCustomParametersType())
                                .setDimensions(agentForm.getDimensionShape())
                                .setShape(shapeEntity)
                                .setType(agentTypeEntity)
                                .setUser(authUser)
                                .setHash(hash);

                        // Remove layers from the agent
                        possibleEntityById.getLayers().clear();
                        possibleEntityById.getLayers().addAll(createLayers(agentForm.getLayers(), possibleEntityById));

                        agentService.save(possibleEntityById);
                    }
                }
            } else {
                this.setHashErrors(result);
                model.addAttribute("add", false);
                return "/agent/add-edit";
            }

            return "redirect:/agent";
        } else
            throw new ForbiddenException("The user is trying to access a forbidden resource!");
    }

    @PostMapping(path = "/edit/{id}", params = "addLayer")
    public String addLayer(@PathVariable("id") Integer id, final AgentForm agentForm, BindingResult result, Model model) {
        agentForm.getLayers().add(new LayerForm());
        agentForm.setMapIdShapeEntity(getAllShapes());
        agentForm.setMapIdTypeEntity(getAllTypes());
        model.addAttribute("add", false);
        return "/agent/add-edit";
    }

    @PostMapping(path = "/edit/{id}", params = "deleteLayer")
    public String deleteLayer(@PathVariable("id") Integer id, @ModelAttribute("agentForm") @Valid AgentForm agentForm,
                              BindingResult result, final HttpServletRequest req, Model model) {
        final Integer index = Integer.valueOf(req.getParameter("deleteLayer"));
        agentForm.getLayers().remove(index.intValue());
        agentForm.setMapIdShapeEntity(getAllShapes());
        agentForm.setMapIdTypeEntity(getAllTypes());
        model.addAttribute("add", false);
        return "/agent/add-edit";
    }

    @PostMapping(path = "/edit/{id}", params = "delAllLayers")
    public String deleteAllLayers(@PathVariable("id") Integer id, @ModelAttribute("agentForm") @Valid AgentForm agentForm,
                                  BindingResult result, Model model) {
        agentForm.getLayers().clear();
        agentForm.setMapIdShapeEntity(getAllShapes());
        agentForm.setMapIdTypeEntity(getAllTypes());
        model.addAttribute("add", false);
        return "/agent/add-edit";
    }

    @GetMapping(path = {"/add"})
    public String add(Model model) throws NullPointerException {
        // Fill the form with the info of the retrieved agent
        AgentForm agentForm = new AgentForm();
        agentForm.setMapIdShapeEntity(this.getAllShapes());
        agentForm.setMapIdTypeEntity(this.getAllTypes());

        // Pass the variables to the model
        model.addAttribute("agentForm", agentForm);
        model.addAttribute("add", true);

        return "agent/add-edit";
    }

    @PostMapping(path = "/add")
    public String add(@ModelAttribute("agentForm") @Valid AgentForm agentForm, BindingResult result, Model model) {
        // Create the hash with the type and the schema
        String hash = Functions.doHash(agentForm.getName() + agentForm.getMolecularWeight()
                + agentForm.getDiffusionRate() + agentForm.getCustomParametersType() + agentForm.getDimensionShape());

        // Set form variables
        Map<Integer, ShapeEntity> allShapes = this.getAllShapes();
        Map<Integer, AgentTypeEntity> allTypes = this.getAllTypes();
        agentForm.setMapIdShapeEntity(allShapes);
        agentForm.setMapIdTypeEntity(allTypes);

        // Check if the agent already exists
        AgentEntity possibleEntity = agentService.findByHash(hash);
        if (possibleEntity == null) {
            // Check if the generated jsons are valid against their schemas
            validateJSONs(agentForm, result, allShapes, allTypes);

            // Go to the form again if there is an error
            if (result.hasErrors()) {
                model.addAttribute("add", true);
                return "/agent/add-edit";
            } else {
                // Get agent type and shape dto to be included in the agent
                AgentTypeEntity agentTypeEntity = agentTypeService.findById(agentForm.getSelectedType());
                ShapeEntity shapeEntity = shapeService.findById(agentForm.getSelectedShape());

                // Get the current user
                UserEntity userEntity = this.getAuthenticatedUser();

                // If ID is -1, then a new type is being created
                AgentEntity agentEntity = new AgentEntity()
                        .setName(agentForm.getName())
                        .setMolecularWeight(agentForm.getMolecularWeight())
                        .setDiffusionRate(agentForm.getDiffusionRate())
                        .setColor(agentForm.getColor())
                        .setCustomParameters(agentForm.getCustomParametersType())
                        .setDimensions(agentForm.getDimensionShape())
                        .setShape(shapeEntity)
                        .setType(agentTypeEntity)
                        .setUser(userEntity)
                        .setHash(hash);
                // Add agent layers
                agentEntity.getLayers().addAll(createLayers(agentForm.getLayers(), agentEntity));

                agentService.save(agentEntity);
            }
        } else {
            this.setHashErrors(result);
            model.addAttribute("add", true);
            return "/agent/add-edit";
        }

        return "redirect:/agent";
    }

    @PostMapping(path = "/add", params = "addLayer")
    public String addLayer(final AgentForm agentForm, BindingResult result, Model model) {
        agentForm.getLayers().add(new LayerForm());
        agentForm.setMapIdShapeEntity(getAllShapes());
        agentForm.setMapIdTypeEntity(getAllTypes());
        model.addAttribute("add", true);
        return "/agent/add-edit";
    }

    @PostMapping(path = "/add", params = "deleteLayer")
    public String deleteLayer(@ModelAttribute("agentForm") @Valid AgentForm agentForm, final HttpServletRequest req,
                              BindingResult result, Model model) {
        final Integer index = Integer.valueOf(req.getParameter("deleteLayer"));
        agentForm.getLayers().remove(index.intValue());
        agentForm.setMapIdShapeEntity(getAllShapes());
        agentForm.setMapIdTypeEntity(getAllTypes());
        model.addAttribute("add", true);
        return "/agent/add-edit";
    }

    @PostMapping(path = "/add", params = "delAllLayers")
    public String deleteAllLayers(@ModelAttribute("agentForm") @Valid AgentForm agentForm, BindingResult result,
                                  Model model) {
        agentForm.getLayers().clear();
        agentForm.setMapIdShapeEntity(getAllShapes());
        agentForm.setMapIdTypeEntity(getAllTypes());
        model.addAttribute("add", true);
        return "/agent/add-edit";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) throws NullPointerException {
        agentService.deleteById(id);
        return "redirect:/agent";
    }

    private Set<LayerEntity> createLayers(List<LayerForm> layerForms, AgentEntity agentEntity) {
        Set<LayerEntity> layerEntities = new HashSet<>();

        // Get layer forms and convert them to entities
        for (LayerForm layerForm : layerForms) {
            LayerEntity layerEntity;
            String name = layerForm.getName();
            String json = layerForm.getDimensionShape();
            String hash = Functions.doHash(layerForm.getName() + layerForm.getDimensionShape());

            // Get layer from database if exists and if the values are the same
            LayerEntity possibleLayer = layerService.findByHash(hash);
            if (possibleLayer != null)
                layerEntity = possibleLayer;
            else {
                // Otherwise, create the layer and set info
                layerEntity = new LayerEntity();
                layerEntity.setName(name);
                layerEntity.setDimensions(json);
                layerEntity.setHash(hash);
                // TODO: 06/05/2020 check if this is null
                layerEntity.setShape(shapeService.findById(layerForm.getSelectedShape()));
                layerEntity.setAgent(agentEntity);
            }

            // Update general variables
            layerEntity.setColor(layerForm.getColor());

            // Add layer to list
            layerEntities.add(layerEntity);
        }

        return layerEntities;
    }

    private void setHashErrors(BindingResult result) {
        result.rejectValue("name", null, "There is already an agent defined with these properties");
        result.rejectValue("molecularWeight", null, "There is already an agent defined with these properties");
        result.rejectValue("diffusionRate", null, "There is already an agent defined with these properties");
        result.rejectValue("customParametersType", null, "There is already an agent defined with these properties");
        result.rejectValue("dimensionShape", null, "There is already an agent defined with these properties");
    }

    private void validateJSONs(AgentForm agentForm, BindingResult result, Map<Integer, ShapeEntity> allShapes,
                               Map<Integer, AgentTypeEntity> allTypes) {
        // Check if the generated type json is valid against its schema
        String json, schema;
        int selectedTypeId = agentForm.getSelectedType();
        if (allTypes.containsKey(selectedTypeId)) {
            schema = allTypes.get(selectedTypeId).getCustomParametersSchema();
            json = agentForm.getCustomParametersType();

            // Put the errors in result variable
            Functions.validateJsonWithSchema(json, schema, "customParametersType", result);
        } else
            result.rejectValue("selectedType", null, "Invalid value selected");

        // Check if the generated shape json is valid against its schema
        int selectedShapeId = agentForm.getSelectedShape();
        if (allShapes.containsKey(selectedShapeId)) {
            schema = allShapes.get(selectedShapeId).getDimensionSchema();
            json = agentForm.getDimensionShape();

            // Put the errors in result variable
            Functions.validateJsonWithSchema(json, schema, "dimensionShape", result);
        } else
            result.rejectValue("selectedShape", null, "Invalid value selected");

        // Check if the generated shape jsons for the layers are valid
        List<LayerForm> layerForms = agentForm.getLayers();
        for (int index = 0; index < layerForms.size(); index++) {
            LayerForm layerForm = layerForms.get(index);

            selectedShapeId = layerForm.getSelectedShape();
            if (allShapes.containsKey(selectedShapeId)) {
                schema = allShapes.get(selectedShapeId).getDimensionSchema();
                json = layerForm.getDimensionShape();

                // Put the errors in result variable
                Functions.validateJsonWithSchema(json, schema, "layers[" + index + "].dimensionShape", result);
            } else
                result.rejectValue("layers[" + index + "].selectedShape", null, "Invalid value selected");
        }
    }

    private boolean canAccessResource(UserEntity authUser, AgentEntity agentEntity) {
        // Admin can edit everything. Regular users can only edit their agents
        if (authUser.getRole().getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)
                || (agentEntity != null && agentEntity.getUser().getId() == authUser.getId()))
            return true;
        else
            return false;
    }

    private UserEntity getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.findByUsername(auth.getName());

        return userEntity;
    }

    private Map<Integer, ShapeEntity> getAllShapes() {
        return shapeService.findAll().stream().collect(Collectors.toMap(ShapeEntity::getId, Function.identity()));
    }

    private Map<Integer, AgentTypeEntity> getAllTypes() {
        return agentTypeService.findAll().stream().collect(Collectors.toMap(AgentTypeEntity::getId, Function.identity()));
    }
}
