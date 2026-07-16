package academy.devdojo.commons;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public static List<User> newUserList() {
        var carlos = User.builder().id(1L).firstName("Carlos").lastName("Soares").email("carlos@gmail.com").build();
        var ana = User.builder().id(2L).firstName("Ana").lastName("Silva").email("ana@gmail.com").build();

        return new ArrayList<>(List.of(carlos, ana));
    }
}
