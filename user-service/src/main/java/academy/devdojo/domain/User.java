package academy.devdojo.domain;

import jakarta.persistence.*;
import lombok.*;

@With // Serve para fazer chamadas encadeadas, consigo atualizar um usuario, facilita o codigo ao inves de usar setter
@Getter
@Setter
@Builder
@EqualsAndHashCode
// Estou mostrando que ele é uma entidade do banco de dados
@Entity
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


}
