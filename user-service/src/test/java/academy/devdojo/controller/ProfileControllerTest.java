package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.domain.Profile;
import academy.devdojo.repository.ProfileRepository;
import academy.devdojo.repository.UserRespository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo"})
class ProfileControllerTest {
    private static final String URL = "/v1/profiles";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProfileRepository repository;
    @MockitoBean
    private UserRespository userRespository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProfileUtils profileUtils;
    private List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("GET v1/profiles - Finding all profiles")
    @Order(1)
    void findAll_ReturnsProfiles_WhenSuccessFul() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var response = fileUtils.readResourceFile("profile/get-all-profiles-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles/1 - Finding profile by id")
    @Order(2)
    void findById_ReturnsProfile_WhenSuccessFul() throws Exception {
        var id = profileList.getFirst().getId();
        var response = fileUtils.readResourceFile("profile/get-find-by-id-1-200.json");

        var profileById = profileList.stream().filter(profile -> profile.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findAll()).thenReturn(profileList);
        BDDMockito.when(repository.findById(id)).thenReturn(profileById);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles/90 - Finding profile by id not exists throw NotFoundException")
    @Order(3)
    void findById_ThrowNotFoundException_WhenIdNotExists() throws Exception {
        var id = 90;
        var response = fileUtils.readResourceFile("profile/get-find-by-id-90-404.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/profiles - Save profile when success ful")
    @Order(4)
    void save_SavesProfile_WhenSuccessFul() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var response = fileUtils.readResourceFile("profile/post-response-profile-201.json");
        var profile = profileList.getFirst();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profile);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource(value = "postProfileBadRequest")
    @DisplayName("POST v1/profiles - Save profile when name and description is empty")
    @Order(5)
    void save_SavesProfile_WhenNameAndDescriptionEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);

    }

    private static Stream<Arguments> postProfileBadRequest() {
        return Stream.of(
                Arguments.of("/post-request-profile-blank-field-400.json", allRequiredErrors())
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequired = "The field 'name' cannot be null";
        var descriptionRequired = "The field 'description' cannot be null";
        return List.of(nameRequired, descriptionRequired);
    }
}