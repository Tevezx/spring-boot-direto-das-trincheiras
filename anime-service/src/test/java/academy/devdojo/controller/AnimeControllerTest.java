package academy.devdojo.controller;

import academy.devdojo.comons.AnimeUtils;
import academy.devdojo.comons.FileUtils;
import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeRepository;
import academy.devdojo.repository.ProducerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
//@ActiveProfiles("test")
class AnimeControllerTest {
    private static final String URL = "/v1/animes";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AnimeRepository repository;
    @MockitoBean
    private ProducerRepository producerRepository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private AnimeUtils animeUtils;

    private List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("GET v1/animes/filterName returns to list with all anime when argument is null")
    @Order(1)
    void listAll_ReturnsAllAnimes_WhenNameIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    // Testando a paginacao
    @Test
    @DisplayName("GET v1/animes/paginated returns a paginated list of animes")
    @Order(1)
    void listAllPaginated_ReturnsAllAnimes_WhenSuccessFul() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-paginated-200.json");
        var pageRequest = PageRequest.of(0, animeList.size());
        var pageAnime = new PageImpl<>(animeList, pageRequest, 1);

        // Qualquer coisa passada que seja da classe Pageable.class, retorno o pageAnime
        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(pageAnime);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/paginated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/filterName?=Naruto returns to list with found object when argument exists")
    @Order(2)
    void findAll_ReturnsFindByName_WhenNameExists() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-naruto-name-200.json");
        var name = "Naruto";
        var expected = animeList.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/filterName?=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        var name = "Sony";

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 returns a anime with given id")
    @Order(4)
    void findByIdOrThrowNotFound_ReturnsAnime_WhenIdFound() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-1-200.json");
        var id = 1L;
        var expected = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/90 throws notFound when anime is not found")
    @Order(5)
    void findByIdOrThrowNotFound_ThrowsThrowNotFound_WhenIdNotFound() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");
        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/animes creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSucessFull() throws Exception {
        var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");
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
        var id = 1L;
        var expected = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/animes/90 throws notFound  when anime is not found")
    @Order(8)
    void deleteById_ThrowNotFound_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");
        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/animes update anime")
    @Order(9)
    void update_UpdateAnime_WhenSucessFull() throws Exception {
        var id = animeList.getFirst();
        var expected = animeList.stream().filter(anime -> anime.getId().equals(id.getId())).findFirst();
        BDDMockito.when(repository.findById(id.getId())).thenReturn(expected);

        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes throws notFound when anime is not found")
    @Order(10)
    void update_ThrowNotFound_WhenAnimeNotFound() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        var response = fileUtils.readResourceFile("anime/put-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/animes returns bad request when fields are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-anime-empty-field-400.json", allRequiredErrors()),
                Arguments.of("post-request-anime-blank-field-400.json", allRequiredErrors())
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-anime-empty-field-400.json", allRequiredErrors()),
                Arguments.of("put-request-anime-blank-field-400.json", allRequiredErrors()),
                Arguments.of("put-request-anime-id-null-400.json", errorIdCannotNull())
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";
        return List.of(nameRequiredError);
    }

    private static List<String> errorIdCannotNull() {
        var idCannotNullError = "The field 'id' cannot be null";
        return List.of(idCannotNullError);
    }
}