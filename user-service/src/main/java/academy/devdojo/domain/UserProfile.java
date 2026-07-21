package academy.devdojo.domain;

import jakarta.persistence.*;
import lombok.*;

@With
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
// Dessa forma, eu defino um nome para o meu entitygraph e quais sao os atributos que eu preciso na minha query
// Facilitando a chamada dele no meu repository
@NamedEntityGraph(name = "UserProfile.fullDetails", attributeNodes = {@NamedAttributeNode("user"), @NamedAttributeNode("profile")})
public class UserProfile {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Muitos ou 1 userProfile podem ter 1 user
    // optional = false -> esse campo é obrigatório
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Profile profile;
}
