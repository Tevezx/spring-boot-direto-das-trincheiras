package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Um repository guarda metodos feitos pelo banco de dados (Consultas, Inserções, Updates e etc)

// @Component -> detecta essa classe como um spring bean, o spring faz um scan e detecta
// Se o spring identifica com bean, ele faz um new pra voce, ou seja, nao precisamos criar as classes
// @Repository -> faz a mesma coisa, pois ele é um component, porém especificamente em repository
// @Service -> faz a mesma coisa, pois ele é um component, porem utilizado em services

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {
    private final static List<Producer> PRODUCERS = new ArrayList<>();

    // Posso chamar como o nome do construtor que eu passo no connection configuration
    private final Connection connectionMongo;

    static {
        var tarantino = Producer.builder().id(1L).name("Tarantino").createdAt(LocalDateTime.now()).build();
        var scorsece = Producer.builder().id(2L).name("Scorsece").createdAt(LocalDateTime.now()).build();
        var walterMello = Producer.builder().id(3L).name("Walter Mello").createdAt(LocalDateTime.now()).build();

        PRODUCERS.addAll(List.of(tarantino, scorsece, walterMello));
    }

    public List<Producer> findAll() {
        log.debug(connectionMongo);
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
