package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestPostProfile {
    @NotBlank(message = "The field 'name' cannot be null")
    private String name;
    @NotBlank(message = "The field 'description' cannot be null")
    private String description;
}
