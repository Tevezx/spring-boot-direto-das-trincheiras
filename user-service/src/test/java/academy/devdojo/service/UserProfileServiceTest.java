package academy.devdojo.service;

import academy.devdojo.commons.UserProfileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.UserProfile;
import academy.devdojo.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {
    @InjectMocks
    private UserProfileService service;
    @Mock
    private UserProfileRepository repository;
    @InjectMocks
    private UserProfileUtils utils;
    private List<UserProfile> userProfileList;

    @BeforeEach
    void init() {
        userProfileList = utils.newUserProfileList();
    }

    @Test
    @DisplayName("Finding all users profiles")
    @Order(1)
    void findAll_ReturnsUsersProfiles_WhenSuccessFul() {
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var userProfileAll = service.findAll();
        Assertions.assertThat(userProfileAll).isNotNull().hasSameElementsAs(userProfileList);
    }

    @Test
    @DisplayName("Finding all users by profile id when id exists")
    @Order(2)
    void findAllUsersByProfileId_ReturnsUser_WhenIdExists() {
        var user = UserUtils.newUserList();
        BDDMockito.when(repository.findAllUsersByProfileId(1L)).thenReturn(user);

        var userProfile = service.findAllUsersByProfileId(1L);

        Assertions.assertThat(userProfile).isNotNull().isNotEmpty().hasSameElementsAs(user);
    }

    @Test
    @DisplayName("Finding all users by profile id when id not exists")
    @Order(3)
    void findAllUsersByProfileId_ReturnsEmpty_WhenIdNotExists() {
        BDDMockito.when(repository.findAllUsersByProfileId(999L)).thenReturn(List.of());
        var userProfile = service.findAllUsersByProfileId(999L);
        Assertions.assertThat(userProfile).isEmpty();
    }
}