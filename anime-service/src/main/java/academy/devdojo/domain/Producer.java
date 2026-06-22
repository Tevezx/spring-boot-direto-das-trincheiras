package academy.devdojo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Producer {
    private Long id;

    // @JsonProperty -> significa que esse atributo quando for passado para o json, ele pode ser escrito com full_name
    // @JsonProperty("full_name")
    private String name;
    private LocalDateTime createdAt;

    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        var tarantino = Producer.builder().id(1L).name("Tarantino").createdAt(LocalDateTime.now()).build();
        var scorsece = Producer.builder().id(2L).name("Scorsece").createdAt(LocalDateTime.now()).build();
        var walterMello = Producer.builder().id(3L).name("Walter Mello").createdAt(LocalDateTime.now()).build();

        producers.addAll(List.of(tarantino, scorsece, walterMello));
    }
}
