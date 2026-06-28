package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;
    @Mock
    private AnimeData animeData;
    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var boruto = Anime.builder().id(2L).name("Boruto").build();
        var attackOnTittan = Anime.builder().id(3L).name("Attack On Tittan").build();

        animeList.addAll(List.of(naruto, boruto, attackOnTittan));
    }

    @Test
    @DisplayName("Finding all animes")
    @Order(1)
    void findAll_returnsAllAnimes_WhenSucessFull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.listAll();
        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(animeList.size());
    }

    @Test
    @DisplayName("List all animes for name")
    @Order(2)
    void listAll_returnsAllAnimeNames_WhenSucessFull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeGetName = animeList.getFirst().getName();
        var animes = repository.listAll();

        Assertions.assertThat(animeGetName).isEqualTo(animes.getFirst().getName());
    }

    @Test
    @DisplayName("List all name returns empty list when name is null")
    @Order(3)
    void listAll_returnsAllAnimeNames_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeGetName = repository.listAllAnimeName(null);
        Assertions.assertThat(animeGetName).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Find by returns a anime with given id")
    @Order(4)
    void findById_returnAnimeById_WhenSucessFull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var anime = animeList.getFirst();
        var animeById = repository.findById(anime.getId());

        Assertions.assertThat(animeById).isPresent().contains(anime);
    }

    @Test
    @DisplayName("Save creates a anime")
    @Order(5)
    void saveAnime_createsAnime_WhenSucessFull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.listAll();
        var anime = Anime.builder().id(5L).name("Boku No Hero").build();
        var animeSave = repository.save(anime);

        Assertions.assertThat(animeSave).hasNoNullFieldsOrProperties().isEqualTo(anime);
        Assertions.assertThat(animes).contains(animeSave).hasSize(4);
    }

    @Test
    @DisplayName("Delete removes a anime by id")
    @Order(6)
    void deleteById_removesAnime_WhenSucessFull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.listAll();
        var anime = animeList.getFirst();
        repository.deleteById(anime.getId());

        var animeById = repository.findById(anime.getId());

        Assertions.assertThat(animeById).isEmpty();
        Assertions.assertThat(animes).doesNotContain(anime);
    }

    @Test
    @DisplayName("Update update a anime")
    @Order(7)
    void update_updateAnime_WhenSucessFull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.listAll();
        var anime = animeList.getFirst();
        anime.setName("Death Note");

        repository.update(anime);

        Assertions.assertThat(animes).contains(anime);
        Assertions.assertThat(anime).isNotNull();

        var animeFindBydId = repository.findById(anime.getId());
        Assertions.assertThat(animeFindBydId).isPresent();

        Assertions.assertThat(animeFindBydId.get().getName()).isEqualTo(anime.getName());
    }
}