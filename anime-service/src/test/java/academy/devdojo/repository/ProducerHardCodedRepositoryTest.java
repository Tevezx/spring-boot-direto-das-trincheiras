package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Utiliza-se o mockito framework para mocar dados para os testes (ou seja, tudo o que esta fora de producer
// repository eu quero mockar/ignorar).
// Porem tem como utilizar o SpringExtension para utilizar dados reais direto dos metodos http
@ExtendWith(MockitoExtension.class)

// Possibilita deixar os testes em ordem
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {
    @InjectMocks // estou fazendo a injecao de dependencias para esse atributo, dessa forma nao preciso dar new nele
    // InjectMocks serve para dar mock na classe que eu quero testar
    private ProducerHardCodedRepository repository;

    // Teste sempre é padrão (nem publico e nem privado e void)

    // Se encontrar esse atributo no repository, mock ele
    @Mock
    private ProducerData producerData;
    private final List<Producer> producerList = new ArrayList<>();

    @BeforeEach
        // esse metodo vai ser executado primeiro do que qualquer outro metodo
    void init() {
        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(2L).name("WitStudio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();

        producerList.addAll(List.of(ufotable, witStudio, studioGhibli));
    }

    // Testando metodo findAll para verificar se ele realmente retorna todos os dados de uma lista
    @Test
    @DisplayName("findAll returns a list with all producers")
    // Definindo a ordem dos testes
    @Order(1)
    void findAll_returnsAllProducers_WhenSucessFul() {
        // Mockito, quando alguem chamar o producerData.getProducers(), retorne a lista de producers inteira
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findAll();

        // Existem dois assertions, o melhor para se utilizar é o assertionsj
        // // verificando se nao é nulo e contem 3 inicias
        Assertions.assertThat(producers).isNotNull().isNotEmpty().hasSize(producerList.size());
    }

    @Test
    @DisplayName("findById returns a producer with given id")
    @Order(2)
    void findById_returnsProducerId_WhenSucessFull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        // Pegando o primeiro id do primeiro elemento
        var expectedProducer = producerList.getFirst();
        var producer = repository.findById(expectedProducer.getId());

        // Verificando se ele é presente (pois ele é um optional no meu repository) e se contain pelo menos um elemento
        Assertions.assertThat(producer).isPresent().contains(expectedProducer);
    }

    // Se o findByName for nulo, retorno vazio
    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_returnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findByName(null);

        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    @Order(4)
    void findByName_returnsFoundProducerInList_WhenNameIsFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducer = producerList.getFirst();
        var producer = repository.findByName(expectedProducer.getName());

        // Espero ao menos um retorno contendo um nome
        Assertions.assertThat(producer).hasSize(1).contains(expectedProducer);
    }

    @Test
    @DisplayName("Save creates a producer")
    @Order(5)
    void save_createsProducer_WhenSucessFull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToSave = Producer.builder()
                .id(5L)
                .name("Sony")
                .createdAt(LocalDateTime.now())
                .build();

        var producer = repository.save(producerToSave);

        // Verificando se o producer salvo é igual ao meu producer criado e se nao tem propriedades nulas
        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        // Verificando se o id esta presente no producer que estou salvando
        var producerGetId = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerGetId).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("Delete removes producer by id")
    @Order(6)
    void deleteById_removesProducer_WhenSucessFull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToDelete = producerList.getFirst();

        repository.deleteById(producerToDelete.getId());

        var producers = repository.findAll();
        // Verificando se na minha lista de producer eu ainda tenho o id do producer deletado
        Assertions.assertThat(producers).doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("Update update a producer")
    @Order(7)
    void update_updateProducer_WhenSucessFull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findAll();
        var producerToUpdate = producerList.getFirst();

        producerToUpdate.setName("Pixar");
        repository.update(producerToUpdate);

        // Verificando se na minha lista de producers tem o producerToUpdate
        Assertions.assertThat(producers).contains(producerToUpdate);

        var producerUpdatedOptional = repository.findById(producerToUpdate.getId());

        // Verificando se o meu id do producer está presente na lista
        Assertions.assertThat(producerUpdatedOptional).isPresent();

        // Verificando se o id presente do nome, é igual ao nome que eu estou alterando em meu producerToUpdate
        Assertions.assertThat(producerUpdatedOptional.get().getName()).isEqualTo(producerToUpdate.getName());
    }
}