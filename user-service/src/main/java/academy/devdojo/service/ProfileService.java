package academy.devdojo.service;

import academy.devdojo.domain.Profile;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<Profile> findAll() {
        return repository.findAll();
    }

    public Profile findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Profile not Found"));
    }

    public Profile save(Profile profile) {
        return repository.save(profile);
    }
}
