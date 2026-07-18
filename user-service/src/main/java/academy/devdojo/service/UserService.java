package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    // private final UserHardCodedRepository repository;
    private final UserRespository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByfirstNameIgnoreCase(name);
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not Found"));
    }

    // Transactional serve para que se caso algo acontecer no meu metodo, um rolback é executado e para tudo aquilo que estava sendo feito antes
    // @Transactional()
    public User save(User user) {
        assertEmailDoesNotExists(user.getEmail());
        return repository.save(user);

//        if (true) {
//               Ponto negativo do transactional:
//               Fazendo chamada para API do SERASA - demora 1 minuto
//               O transactional espera 1m e faz o banco ficar parado durante 1 minuto
//               Em uma aplicação grande com varios usuarios acessando, seria uns 2000 minutos
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
//        }
    }

    public void deleteById(Long id) {
        var user = findById(id);
        repository.deleteById(user.getId());
    }

    public void update(User user) {
        findById(user.getId());
        assertEmailDoesNotExists(user.getEmail(), user.getId());
        repository.save(user);
    }

    public void assertEmailDoesNotExists(String email) {
        repository.findByEmail(email).ifPresent(u -> throwEmailExistsException());
    }

    public void assertEmailDoesNotExists(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(u -> throwEmailExistsException());
    }

    public void throwEmailExistsException() {
        throw new EmailAlreadyExistsException("E-mail already exists");
    }
}
