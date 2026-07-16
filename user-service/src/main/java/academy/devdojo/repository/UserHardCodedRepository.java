package academy.devdojo.repository;

import academy.devdojo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHardCodedRepository {
    private final UserData userData;

    public List<User> findAll() {
        return userData.getUserList();
    }

    public List<User> listAllNames(String firstName) {
        return userData.getUserList().stream().filter(user -> user.getFirstName().equalsIgnoreCase(firstName)).toList();
    }

    public Optional<User> findById(Long id) {
        return userData.getUserList().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public User save(User user) {
        userData.getUserList().add(user);
        return user;
    }

    public void deleteById(Long id) {
        userData.getUserList().removeIf(user -> user.getId().equals(id));
    }

    public void update(User user) {
        deleteById(user.getId());
        save(user);
    }
}
