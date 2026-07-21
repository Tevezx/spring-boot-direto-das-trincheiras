package academy.devdojo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@With // Serve para fazer chamadas encadeadas, consigo atualizar um usuario, facilita o codigo ao inves de usar setter
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Estou mostrando que ele é uma entidade do banco de dados
@Entity
// "user" é palavra reservada no H2, por isso precisamos renomear a tabela para poder realizar os testes
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    // Demonstrando meu primary key auto genarate
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Demonstrando que minhas colunas nao podem ser nulas
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    // Email nao pode ser nulo e deve ser unico
    @Column(nullable = false, unique = true)
    private String email;

    // FetchTypeLazy significa que ele só irá pegar os dados do userProfiles se caso for necessario
    // Ou seja, ira executar apenas a classe user, economizando querys no banco de dados
    // FetchTypeEager significa que ira carregar todos os dados do userProfile, nao economizando querys
    // É utilizado quando necessariamente eu preciso carregar os dados do userProfile
    // Normalmente nao se cria N:N dessa forma colocando @OneToMany, pois fica bidirecional
    // Normalmente se cria outra classe com a anotacao @ManyToOne, pois fica unidirecional
    //@OneToMany(fetch = FetchType.LAZY)
    //@ToString.Exclude
    //private List<UserProfile> userProfiles;

}
