package academy.devdojo.service;

import academy.devdojo.domain.UserProfile;
import academy.devdojo.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.findAll();
    }
}
