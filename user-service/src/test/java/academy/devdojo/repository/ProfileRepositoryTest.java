package academy.devdojo.repository;

import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.config.TestcontainersConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({ProfileUtils.class, TestcontainersConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest extends IntegrationTestConfig {
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
        Assertions.assertThat(profileSave.getId()).isPositive().isNotNull();
    }

    @Test
    @DisplayName("Find all profiles when success ful")
    @Order(2)
    @Sql("/sql/profile/init_one_profile.sql")
    void findAll_ReturnsProfiles_WhenSuccessFul() {
        var profiles = repository.findAll();
        Assertions.assertThat(profiles).isNotEmpty();
    }
}