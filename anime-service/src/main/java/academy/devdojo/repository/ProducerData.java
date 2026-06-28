package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ProducerData {
    private final List<Producer> producers = new ArrayList<>();

    {
        var tarantino = Producer.builder().id(1L).name("Tarantino").createdAt(LocalDateTime.now()).build();
        var scorsece = Producer.builder().id(2L).name("Scorsece").createdAt(LocalDateTime.now()).build();
        var walterMello = Producer.builder().id(3L).name("Walter Mello").createdAt(LocalDateTime.now()).build();

        producers.addAll(List.of(tarantino, scorsece, walterMello));
    }
}
