package academy.devdojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestPutUser {
    @NotNull(message = "The field 'id' cannot be null")
    @Schema(description = "User's id", example = "1")
    private Long id;
    @NotBlank(message = "The field 'firstName' is required")
    @Schema(description = "User's first name", example = "Carlos")
    private String firstName; // nao seja nulo, nem vazio e nem branco
    @NotBlank(message = "The field 'lastName' is required")
    @Schema(description = "User's last name", example = "Soares")
    private String lastName;
    // Essa validacao obriga colocar os campos do email
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "e-mail is not valid")
    @Schema(description = "User's email", example = "carlos@gmail.com")
    private String email;
}
