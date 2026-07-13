package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.listAllNames(name);
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(User user) {
        findById(user.getId());
        repository.update(user);
    }
}
