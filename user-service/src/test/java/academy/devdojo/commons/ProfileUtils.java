package academy.devdojo.commons;

import academy.devdojo.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileUtils {
    public List<Profile> newProfileList() {
        var carlos = Profile.builder().id(1L).name("Administrador").description("Perfil de Administrador").build();
        var ana = Profile.builder().id(2L).name("Gerente").description("Perfil de Gerente").build();

        return new ArrayList<>(List.of(carlos, ana));
    }

    public Profile newProfileToSave() {
        return Profile.builder().name("Administrador").description("Perfil de Administrador").build();
    }
}
