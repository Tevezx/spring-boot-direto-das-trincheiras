package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Producer {
    private Long id;

    // @JsonProperty -> significa que esse atributo quando for passado para o json, ele pode ser escrito com full_name
    // @JsonProperty("full_name")
    private String name;

    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        var tarantino = new Producer(1L, "Tarantino");
        var scorsece = new Producer(2L, "Scorsece");
        var walterMello = new Producer(3L, "Walter Mello");

        producers.addAll(List.of(tarantino, scorsece, walterMello));
    }
}
