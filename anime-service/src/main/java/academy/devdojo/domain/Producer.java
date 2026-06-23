package academy.devdojo.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        var tarantino = Producer.builder().id(1L).name("Tarantino").createdAt(LocalDateTime.now()).build();
        var scorsece = Producer.builder().id(2L).name("Scorsece").createdAt(LocalDateTime.now()).build();
        var walterMello = Producer.builder().id(3L).name("Walter Mello").createdAt(LocalDateTime.now()).build();

        producers.addAll(List.of(tarantino, scorsece, walterMello));
    }
}
