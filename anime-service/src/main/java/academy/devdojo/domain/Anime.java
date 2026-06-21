package academy.devdojo.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Anime {
    private final Long id;
    private final String name;

    public Anime(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<Anime> animeList() {
        Anime anime1 = new Anime(1L, "Naruto");
        Anime anime2 = new Anime(2L, "Boruto");
        Anime anime3 = new Anime(3L, "Attack On Titan");

        return List.of(anime1, anime2, anime3);
    }
}
