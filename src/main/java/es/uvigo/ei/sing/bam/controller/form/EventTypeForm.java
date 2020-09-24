package es.uvigo.ei.sing.bam.controller.form;

import es.uvigo.ei.sing.bam.util.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EventTypeForm {
    @Size(min = 1, max = 45, message = "The lenght must be between 1 and 45 characters")
    private String type;
    @NotEmpty(message = Constants.NOT_EMPTY)
    private String description;
    @NotEmpty(message = Constants.NOT_EMPTY)
    private String customInformationSchema;
}
