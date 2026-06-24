package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var boruto = Anime.builder().id(2L).name("Boruto").build();
        var attackOnTittan = Anime.builder().id(3L).name("Attack On Tittan").build();

        ANIMES.addAll(List.of(naruto, boruto, attackOnTittan));
    }

    public List<Anime> listAll(){
        return ANIMES;
    }

    public List<Anime> listAllAnimeName(String name){
        return ANIMES.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public Optional<Anime> findById(Long id){
        return ANIMES.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public Anime save(Anime anime){
        ANIMES.add(anime);
        return anime;
    }

    public void deleteById(Long id){
        ANIMES.removeIf(anime -> anime.getId().equals(id));
    }

    public void update(Anime anime){
        deleteById(anime.getId());
        save(anime);
    }

}
