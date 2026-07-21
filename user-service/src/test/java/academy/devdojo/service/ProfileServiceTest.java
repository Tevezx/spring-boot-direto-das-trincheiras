package academy.devdojo.service;

import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.domain.Profile;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;
    @Mock
    private ProfileRepository repository;
    @InjectMocks
    private ProfileUtils profileUtils;
    private List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("Finding all profiles")
    @Order(1)
    void findAll_ReturnsProfiles_WhenSuccessFul() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var profiles = service.findAll();
        Assertions.assertThat(profiles).containsExactlyElementsOf(profileList);
    }

    @Test
    @DisplayName("Finding profile by id exists")
    @Order(2)
    void findById_ReturnsProfile_WhenSuccessFul() {
        var profile = profileList.getFirst();
        BDDMockito.when(repository.findById(profile.getId())).thenReturn(Optional.of(profile));

        var profileById = service.findById(profile.getId());
        Assertions.assertThat(profileById).isNotNull().isEqualTo(profile);
    }

    @Test
    @DisplayName("Finding profile by id not exists - Throw NotFoundException")
    @Order(3)
    void findById_ThrowNotFoundException_WhenIdNotExists() {
        var profile = profileList.getFirst();
        BDDMockito.when(repository.findById(profile.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findById(profile.getId())).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Saving profile when success ful")
    @Order(4)
    void save_SavesProfile_WhenSuccessFul() {
        var profile = profileList.getFirst();
        BDDMockito.when(repository.save(profile)).thenReturn(profile);

        var profileSaved = service.save(profile);
        Assertions.assertThat(profileSaved).hasNoNullFieldsOrProperties().isEqualTo(profile);
    }

}