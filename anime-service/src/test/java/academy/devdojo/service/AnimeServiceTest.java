package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeHardCodedRepository repository;
    private List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var boruto = Anime.builder().id(2L).name("Boruto").build();
        var attackOnTittan = Anime.builder().id(3L).name("Attack On Tittan").build();

        animeList.addAll(List.of(naruto, boruto, attackOnTittan));
    }

    @Test
    @DisplayName("FindAll returns to list with all anime when argument is null")
    @Order(1)
    void listAll_ReturnsAllAnimes_WhenNameIsNull() {
        BDDMockito.when(repository.listAll()).thenReturn(animeList);
        var animes = service.listAll(null);

        // O que retornar de animes nao pode ser null e tem que ser igual a minha lista de animes
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("FindAll returns to list with found object when argument exists")
    @Order(2)
    void findAll_ReturnsFindByName_WhenNameExists() {
        var anime = animeList.getFirst();
        List<Anime> animeExpected = animeList
                .stream()
                .filter(anime1 -> anime1.getName().equalsIgnoreCase(anime.getName()))
                .toList();

        BDDMockito.when(repository.listAllAnimeName(anime.getName())).thenReturn(animeExpected);

        List<Anime> animeResponse = service.listAll(anime.getName());
        Assertions.assertThat(animeResponse).isNotNull().isNotEmpty().containsExactlyElementsOf(animeExpected);
    }

    @Test
    @DisplayName("FindAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        BDDMockito.when(repository.listAllAnimeName("not-found")).thenReturn(Collections.emptyList());
        List<Anime> anime = service.listAll("not-found");

        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("FindById returns a anime with given id")
    @Order(4)
    void findByIdOrThrowNotFound_ReturnsAnime_WhenIdFound() {
        var anime = animeList.getFirst();

        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));
        var animeResponse = service.findByIdOrThrowNotFound(anime.getId());

        Assertions.assertThat(animeResponse).isNotNull().isEqualTo(anime);
    }

    @Test
    @DisplayName("FindById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findByIdOrThrowNotFound_ThrowsResponseStatusException_WhenIdNotFound() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(anime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Save creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSucessFull() {
        var anime = Anime.builder().id(5L).name("Boku No Hero").build();
        BDDMockito.when(repository.save(anime)).thenReturn(anime);

        var animeSave = service.save(anime);
        Assertions.assertThat(animeSave).isEqualTo(anime).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Delete removes anime by id")
    @Order(7)
    void deleteById_RemovesAnime_WhenSucessFull() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));

        service.deleteById(anime.getId());
        Assertions.assertThatNoException().isThrownBy(() -> service.deleteById(anime.getId()));
    }

    @Test
    @DisplayName("Delete throws ResponseStatusException when anime is not found")
    @Order(8)
    void deleteById_ResponseStatusException_WhenAnimeIsNotFound() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.deleteById(anime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Update update anime")
    @Order(9)
    void update_UpdateAnime_WhenSucessFull() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));

        service.update(anime);
        Assertions.assertThatNoException().isThrownBy(() -> service.update(anime));
    }

    @Test
    @DisplayName("Update throws ResponseStatusException when anime is not found")
    @Order(10)
    void update_ResponseStatusException_WhenAnimeNotFound() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(anime))
                .isInstanceOf(ResponseStatusException.class);
    }
}