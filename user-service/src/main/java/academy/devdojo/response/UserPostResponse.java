package academy.devdojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class UserPostResponse {
    @Schema(description = "User's id", example = "1")
    private Long id;
    @Schema(description = "User's first name", example = "Carlos")
    private String firstName;
    @Schema(description = "User's last name", example = "Soares")
    private String lastName;
    @Schema(description = "User's email", example = "carlos@gmail.com")
    private String email;
}
