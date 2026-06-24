package academy.devdojo.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
// onlyExplicitlyIncluded = true -> significa que estou habilitando a opcao de detectar algo que é igual (nesse caso o id)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
    // Ou seja, se tiver o mesmo id são producers iguais
    @EqualsAndHashCode.Include
    private Long id;

    // @JsonProperty -> significa que esse atributo quando for passado para o json, ele pode ser escrito com full_name
    // @JsonProperty("full_name")
    private String name;
    private LocalDateTime createdAt;
}
