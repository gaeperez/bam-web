package es.uvigo.ei.sing.bam.controller.form;

import es.uvigo.ei.sing.bam.entity.AgentTypeEntity;
import es.uvigo.ei.sing.bam.entity.ShapeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AgentForm {
    @Size(min = 1, max = 100, message = "The name size must be greater than 1 and lesser than 100")
    private String name;
    @Positive(message = "Only numbers greater than 0 are allowed")
    private float molecularWeight;
    @NotNull(message = "The diffusion rate cannot be null")
    private double diffusionRate;
    @NotNull(message = "No color selected")
    private String color;
    @NotNull(message = "No shape selected")
    private int selectedShape;
    private String dimensionShape;
    @NotNull(message = "No type selected")
    private int selectedType;
    private String customParametersType;
    @Valid
    private List<LayerForm> layers = new ArrayList<>();

    // Variables to show list the entities in the form
    private Map<Integer, ShapeEntity> mapIdShapeEntity = new HashMap<>();
    private Map<Integer, AgentTypeEntity> mapIdTypeEntity = new HashMap<>();
}
