package academy.devdojo.service;

import academy.devdojo.comons.ProducerUtils;
import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;

    @Mock
    private ProducerHardCodedRepository repository;
    private List<Producer> producerList = new ArrayList<>();

    @InjectMocks
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("FindAll returns to list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenNameIsNull() {
        // Quando eu chamar o findAll, retorno a lista toda
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var producers = service.findAll(null);
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("FindAll returns to list with found object when argument exists")
    @Order(2)
    void findAll_ReturnsFindByName_WhenNameExists() {
        Producer producerFindByName = producerList.getFirst();
        List<Producer> expected = producerList
                .stream()
                .filter(producer -> producer.getName().equalsIgnoreCase(producerFindByName.getName()))
                .toList();

        BDDMockito.when(repository.findByName(producerFindByName.getName())).thenReturn(expected);

        var producers = service.findAll(producerFindByName.getName());

        Assertions.assertThat(producers).isNotNull().isNotEmpty().containsExactlyElementsOf(expected);
    }

    @Test
    @DisplayName("FindAll returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        // Quando eu passar not-found, quero que ele retorne uma lista vazia
        BDDMockito.when(repository.findByName("not-found")).thenReturn(Collections.emptyList());

        var producers = service.findAll("not-found");
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("FindById returns a producer with given id")
    @Order(4)
    void findById_ReturnsProducer_WhenIdFound() {
        var producerExpected = producerList.getFirst();

        // Se o meu id que estou procurando existe, retorno exatamente quem ele é
        BDDMockito.when(repository.findById(producerExpected.getId())).thenReturn(Optional.of(producerExpected));

        var producers = service.findByIdOrThrowNotFound(producerExpected.getId());

        Assertions.assertThat(producers).isNotNull().isEqualTo(producerExpected);
    }

    @Test
    @DisplayName("FindById throws ResponseStatusException when producer is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        var producerExpected = producerList.getFirst();
        // Se ele nao existe, retorna um Optional vazio
        BDDMockito.when(repository.findById(producerExpected.getId())).thenReturn(Optional.empty());

        // Verificando se ele retorna exatamente uma excecao da classe ResponseStatusException
        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(producerExpected.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Save creates a producer")
    @Order(6)
    void save_createsProducer_WhenSucessFull() {
        var producerToSave = Producer.builder()
                .id(5L)
                .name("Sony")
                .createdAt(LocalDateTime.now())
                .build();

        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var producer = service.save(producerToSave);

        // Verificando se o producer salvo é igual ao meu producer criado e se nao tem propriedades nulas
        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Delete removes producer by id")
    @Order(7)
    void deleteById_removesProducer_WhenSucessFull() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));

        // Verificando se nenhuma excecao foi lancada
        Assertions.assertThatNoException().isThrownBy(() -> service.deleteById(producerToDelete.getId()));
    }

    @Test
    @DisplayName("Delete throws ResponseStatusException when producer is not found")
    @Order(8)
    void deleteById_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());

        // Verificando se a excecao devida do meu metodo deletebyid esta retornando corretamente caso eu passe um parametro vazio
        Assertions.assertThatException().isThrownBy(() -> service.deleteById(producerToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Update update a producer")
    @Order(9)
    void update_updateProducer_WhenSucessFull() {
        var producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Pixar");
        producerToUpdate.setName("Pixar");
        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));

        service.update(producerToUpdate);
        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));
    }

    @Test
    @DisplayName("Update throws ResponseStatusException when producer is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        var producerToUpdate = producerList.getFirst();
        // Independente do que voce passar para o repository.findById, retorne vazio
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(producerToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}