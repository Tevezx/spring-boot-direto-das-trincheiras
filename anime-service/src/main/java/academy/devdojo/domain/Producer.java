package academy.devdojo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
// onlyExplicitlyIncluded = true -> significa que estou habilitando a opcao de detectar algo que é igual (nesse caso o id)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Producer {
    @Id
    // Ou seja, se tiver o mesmo id são producers iguais
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @JsonProperty -> significa que esse atributo quando for passado para o json, ele pode ser escrito com full_name
    // @JsonProperty("full_name")
    @Column(nullable = false)
    private String name;
    // Nao consigo criar e nem atualizar, pois é algo criado direto no momento do post
    @Column(nullable = false, insertable = false, updatable = false)
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
}
