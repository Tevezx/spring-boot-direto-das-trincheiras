package academy.devdojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfileGetResponse {
    @Schema(description = "Profile's id", example = "1")
    private Long id;
    @Schema(description = "Profile's name", example = "User default")
    private String name;
    @Schema(description = "Profile's description", example = "Profile default")
    private String description;
}
