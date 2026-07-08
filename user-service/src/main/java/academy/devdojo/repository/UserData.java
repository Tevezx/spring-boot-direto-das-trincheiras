package academy.devdojo.repository;

import academy.devdojo.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class UserData {
    private final List<User> userList = new ArrayList<>();

    {
        var carlos = User.builder().id(1L).firstName("Carlos").lastName("Soares").email("carlos@gmail.com").build();
        var ana = User.builder().id(2L).firstName("Ana").lastName("Silva").email("ana@gmail.com").build();

        userList.addAll(List.of(carlos, ana));
    }
}
