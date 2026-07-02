package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class AnimeHardCodedRepository {
    private final AnimeData animeData;
    private final Connection connection;

    public List<Anime> listAll() {
        log.debug("Connection: {}", connection);
        return animeData.getAnimes();
    }

    public List<Anime> listAllAnimeName(String name) {
        return animeData.getAnimes().stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public Optional<Anime> findById(Long id) {
        return animeData.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public Anime save(Anime anime) {
        animeData.getAnimes().add(anime);
        return anime;
    }

    public void deleteById(Long id) {
        animeData.getAnimes().removeIf(anime -> anime.getId().equals(id));
    }

    public void update(Anime anime) {
        deleteById(anime.getId());
        save(anime);
    }

}
