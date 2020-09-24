package es.uvigo.ei.sing.bam.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LayerForm {
    @Size(min = 1, max = 100, message = "The name size must be greater than 1 and lesser than 100")
    private String name;
    @NotNull(message = "No color selected")
    private String color;
    @NotNull(message = "No shape selected")
    private int selectedShape;
    private String dimensionShape;
    private String hash;
}
