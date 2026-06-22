package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;

    @Getter
    private static List<Anime> animeList = new ArrayList<>();

    static {
        Anime anime1 = new Anime(1L, "Naruto");
        Anime anime2 = new Anime(2L, "Boruto");
        Anime anime3 = new Anime(3L, "Attack On Titan");

        animeList.addAll(List.of(anime1, anime2, anime3));
    }

}
