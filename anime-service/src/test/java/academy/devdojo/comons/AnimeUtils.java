package academy.devdojo.comons;

import academy.devdojo.domain.Anime;
import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {
    public List<Anime> newAnimeList() {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var boruto = Anime.builder().id(2L).name("Boruto").build();
        var attackOnTittan = Anime.builder().id(3L).name("Attack On Tittan").build();

        return new ArrayList<>(List.of(naruto, boruto, attackOnTittan));
    }
}
