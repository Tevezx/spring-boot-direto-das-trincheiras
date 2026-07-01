package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoSpyBean
    private AnimeHardCodedRepository repository;
    @MockitoBean
    private AnimeData animeData;
    @Autowired
    private ResourceLoader resourceLoader;

    private List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var boruto = Anime.builder().id(2L).name("Boruto").build();
        var attackOnTittan = Anime.builder().id(3L).name("Attack On Tittan").build();

        animeList.addAll(List.of(naruto, boruto, attackOnTittan));
    }

    @Test
    @DisplayName("GET v1/animes/filterName returns to list with all anime when argument is null")
    @Order(1)
    void listAll_ReturnsAllAnimes_WhenNameIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/filterName"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/filterName?=Naruto returns to list with found object when argument exists")
    @Order(2)
    void findAll_ReturnsFindByName_WhenNameExists() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var response = readResourceFile("anime/get-anime-naruto-name-200.json");
        var name = "Naruto";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/filterName").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/filterName?=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var response = readResourceFile("anime/get-anime-x-name-200.json");
        var name = "Sony";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/filterName").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 returns a anime with given id")
    @Order(4)
    void findByIdOrThrowNotFound_ReturnsAnime_WhenIdFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var response = readResourceFile("anime/get-anime-by-id-1-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/90 throws ResponseStatusException when anime is not found")
    @Order(5)
    void findByIdOrThrowNotFound_ThrowsResponseStatusException_WhenIdNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var id = 90L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @DisplayName("POST v1/animes creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSucessFull() throws Exception {
        var request = readResourceFile("anime/post-request-anime-200.json");
        var response = readResourceFile("anime/post-response-anime-201.json");
        var anime = Anime.builder().id(90L).name("Boku no Hero").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(anime);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/animes/1 removes anime by id")
    @Order(7)
    void deleteById_RemovesAnime_WhenSucessFull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/animes/90 throws ResponseStatusException when anime is not found")
    @Order(8)
    void deleteById_ResponseStatusException_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @DisplayName("PUT v1/animes update anime")
    @Order(9)
    void update_UpdateAnime_WhenSucessFull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = readResourceFile("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes throws ResponseStatusException when anime is not found")
    @Order(10)
    void update_ResponseStatusException_WhenAnimeNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = readResourceFile("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}