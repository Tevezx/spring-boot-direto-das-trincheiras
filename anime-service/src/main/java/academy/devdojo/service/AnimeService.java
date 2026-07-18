package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository repository;

    public List<Anime> listAll(String name) {
        return name != null ? repository.findByNameIgnoreCase(name) : repository.findAll();
    }

    public Page<Anime> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Anime not Found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void deleteById(Long id) {
        var animeDeleted = findByIdOrThrowNotFound(id);
        repository.deleteById(animeDeleted.getId());
    }

    public void update(Anime anime) {
        findByIdOrThrowNotFound(anime.getId());
        repository.save(anime);
    }
}
