package academy.devdojo.repository;

import academy.devdojo.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Um repository guarda metodos feitos pelo banco de dados (Consultas, Inserções, Updates e etc)

public class ProducerHardCodedRepository {
    private final static List<Producer> PRODUCERS = new ArrayList<>();

    static {
        var tarantino = Producer.builder().id(1L).name("Tarantino").createdAt(LocalDateTime.now()).build();
        var scorsece = Producer.builder().id(2L).name("Scorsece").createdAt(LocalDateTime.now()).build();
        var walterMello = Producer.builder().id(3L).name("Walter Mello").createdAt(LocalDateTime.now()).build();

        PRODUCERS.addAll(List.of(tarantino, scorsece, walterMello));
    }

    public List<Producer> findAll() {
        return PRODUCERS;
    }

    // Retorna um optional pelo fato de que estou retornando um orElseThrow
    public Optional<Producer> findById(Long id) {
        return PRODUCERS.stream().filter(producer -> producer.getId().equals(id)).findFirst();
    }

    public List<Producer> findByName(String name) {
        return PRODUCERS.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    public Producer save(Producer producer) {
        PRODUCERS.add(producer);
        return producer;
    }

    public void deleteById(Long id) {
        PRODUCERS.removeIf(producerObj -> producerObj.getId().equals(id));
    }

    public void update(Producer producer) {
        deleteById(producer.getId());
        save(producer);
    }
}
