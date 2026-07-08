package academy.devdojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestPostUser {
    // Isso é chamado de bean validation
    // Colocamos os bean validation nos request, pois estamos expondo ao usuario e precisamos verificar os dados
    @NotBlank(message = "The field 'firstName' is required")
    private String firstName; // nao seja nulo, nem vazio e nem branco
    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;
    // Essa validacao obriga colocar os campos do email
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "e-mail is not valid")
    private String email;
}
