package academy.devdojo.repository;

import academy.devdojo.commons.ProfileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(ProfileUtils.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository repository;
    @Autowired
    private ProfileUtils utils;

    @Test
    @DisplayName("Saving profile when success ful")
    @Order(1)
    void save_CreatesUser_WhenSuccessFul() {
        var profile = utils.newProfileToSave();
        var profileSave = repository.save(profile);

        Assertions.assertThat(profileSave).hasNoNullFieldsOrProperties();
        Assertions.assertThat(profileSave.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Find all profiles when success ful")
    @Order(2)
    @Sql("/sql/init_one_profile.sql")
    void findAll_ReturnsProfiles_WhenSuccessFul() {
        var profiles = repository.findAll();
        Assertions.assertThat(profiles).isNotEmpty();
    }
}