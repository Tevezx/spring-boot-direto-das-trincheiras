package academy.devdojo.commons;

import academy.devdojo.domain.Profile;
import academy.devdojo.domain.User;
import academy.devdojo.domain.UserProfile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserProfileUtils {
    private final List<User> users = UserUtils.newUserList();
    private final List<Profile> profiles = ProfileUtils.newProfileList();

    public List<UserProfile> newUserProfileList() {
        var administradorCarlos = UserProfile.builder().id(1L).user(users.getFirst()).profile(profiles.getFirst()).build();
        var gerenteAna = UserProfile.builder().id(2L).user(users.get(1)).profile(profiles.get(1)).build();

        return new ArrayList<>(List.of(administradorCarlos, gerenteAna));
    }

    public UserProfile newUserProfileToSave() {
        return UserProfile.builder().user(users.getFirst()).profile(profiles.getFirst()).build();
    }
}
