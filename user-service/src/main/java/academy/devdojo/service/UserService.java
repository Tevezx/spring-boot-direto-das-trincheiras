package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public User save(User user) {
        assertEmailDoesNotExists(user.getEmail());
        return repository.save(user);
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
