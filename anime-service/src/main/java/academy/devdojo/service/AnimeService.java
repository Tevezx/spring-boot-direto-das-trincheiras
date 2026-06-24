package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnimeService {
    private final AnimeHardCodedRepository repository;

    @Autowired
    public AnimeService(AnimeHardCodedRepository repository) {
        this.repository = repository;
    }

    public List<Anime> listAll(String name) {
        return name != null ? repository.listAllAnimeName(name) : repository.listAll();
    }

    public Anime findByIdOrThrowNotFound(Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Anime save(Anime anime){
        return repository.save(anime);
    }

    public void deleteById(Long id){
        Anime animeDeleted = findByIdOrThrowNotFound(id);
        repository.deleteById(animeDeleted.getId());
    }

    public void update(Anime anime){
        Anime animeUpdate = findByIdOrThrowNotFound(anime.getId());
        repository.update(animeUpdate);
    }
}
