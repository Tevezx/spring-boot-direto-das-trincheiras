package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserProfileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.UserProfile;
import academy.devdojo.repository.ProfileRepository;
import academy.devdojo.repository.UserProfileRepository;
import academy.devdojo.repository.UserRespository;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = UserProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo"})
class UserProfileControllerTest {
    private final static String URL = "/v1/user-profiles";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserProfileRepository repository;
    @MockitoBean
    private UserRespository userRespository;
    @MockitoBean
    private ProfileRepository profileRepository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserProfileUtils userProfileUtils;
    private List<UserProfile> userProfileList = new ArrayList<>();

    @BeforeEach
    void init() {
        userProfileList = userProfileUtils.newUserProfileList();
    }

    @Test
    @DisplayName("GET v1/user-profiles - finding all users profiles")
    @Order(1)
    void findAll_ReturnsUsersProfiles_WhenSuccessFul() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var response = fileUtils.readResourceFile("user-profile/get-user-profile-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/user-profiles/profiles/{id}/users - finding all users profiles by id")
    @Order(2)
    void findAllUsersByProfileId_ReturnsUsersProfiles_WhenSuccessFul() throws Exception {
        var users = UserUtils.newUserList();
        BDDMockito.when(repository.findAllUsersByProfileId(1L)).thenReturn(users);

        var response = fileUtils.readResourceFile("user-profile/get-user-profile-by-id-response-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/profiles/{id}/users", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
}