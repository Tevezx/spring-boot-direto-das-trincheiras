package academy.devdojo.repository;

import academy.devdojo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// CrudRepository tem as operações basicas de um crud, ele pede qual o domain e qual o tipo de id desse domain
// JpaRepository tem os mesmos metodos, porem com mais opcoes
public interface UserRespository extends JpaRepository<User, Long> {
    List<User> findByfirstNameIgnoreCase(String firstName);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIdNot(String email, Long id);
}
