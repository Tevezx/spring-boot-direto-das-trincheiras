package academy.devdojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestPostProfile {
    @NotBlank(message = "The field 'name' cannot be null")
    @Schema(description = "Profile name", example = "User default")
    private String name;
    @NotBlank(message = "The field 'description' cannot be null")
    @Schema(description = "Profile description", example = "Profile default")
    private String description;
}
