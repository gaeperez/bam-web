package es.uvigo.ei.sing.bam.controller.form;

import es.uvigo.ei.sing.bam.util.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ShapeForm {
    @Size(min = 1, max = 45, message = "The lenght must be between 1 and 45 characters")
    private String form;
    @NotEmpty(message = Constants.NOT_EMPTY)
    private String dimensionSchema;
}
