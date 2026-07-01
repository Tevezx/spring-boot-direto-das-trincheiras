package academy.devdojo.controller;

// Serve para startar um servidor para poder realizar os testes (é mais utilizado em testes de integracao)
// Startando como se tivesse rodando minha aplicacao normal na porta mockada
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)

// Serve para poder fazer uma chamada rest
//@AutoConfigureMockMvc

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Meio termo entre teste unitario e teste de integracao (slices test)
// Interessante utilizar quando voce quer que seja testes mais rapidos para testar o controller
@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
// Faz a mesma coisa que o @Import, so que ja importa todas as classes do meu pacote academy.devdojo
@ComponentScan(basePackages = "academy.devdojo")
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Faz a mesma coisa que @Mock
    @MockitoBean // Ao invés de utilizar a lista do producerData, ele vai utilizar a lista do void init() mockada
    private ProducerData producerData;

    // estou mockando apenas um determinado metodo, no caso: save
    @MockitoSpyBean
    private ProducerHardCodedRepository repository;

    private final List<Producer> producerList = new ArrayList<>();

    // Carregar um arquivo
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
        // esse metodo vai ser executado primeiro do que qualquer outro metodo
    void init() {
        // Fazendo dessa forma para que todos tenham a mesma data para poder realizar os testes via json
        var dateTime = "2026-06-30T10:07:56.8602558";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("WitStudio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();

        producerList.addAll(List.of(ufotable, witStudio, studioGhibli));
    }

    @Test
    @DisplayName("GET v1/producers returns to list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenNameIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        // Digo qual o json que eu espero como resposta da requisicao
        var response = readResourceFile("producer/get-producer-null-name-200.json");

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok e leita meu arquivo response
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns to list with found object when argument exists")
    @Order(2)
    void findAll_ReturnsFindByName_WhenNameExists() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        // Digo qual o json que eu espero como resposta da requisicao
        var response = readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("v1/producers?name=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenIsNameNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        // Digo qual o json que eu espero como resposta da requisicao
        var response = readResourceFile("producer/get-producer-x-name-200.json");
        // Esse nome nao existe na lista, entao é para retornar a lista vazia segundo o json que eu defini em cima
        var name = "Sony";

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 returns a producer with given id")
    @Order(4)
    void findById_ReturnsProducer_WhenIdFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        // Digo qual o json que eu espero como resposta da requisicao
        var response = readResourceFile("producer/get-producer-by-id-1-200.json");
        // Esse nome nao existe na lista, entao é para retornar a lista vazia segundo o json que eu defini em cima
        var id = 1L;

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/90 throws ResponseStatusException 404 when producer is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = 90L;

        // verificando se o status 404 esta sendo retornado pelo isNotFound e dizendo qual mensagem deve aparecer no body
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not Found"));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_createsProducer_WhenSucessFull() throws Exception {
        var request = readResourceFile("producer/post-request-producer-200.json");
        var response = readResourceFile("producer/post-response-producer-201.json");
        // Eu faço dessa forma esse metodo por conta do createdAt
        var producer = Producer.builder().id(99L).name("Sony").createdAt(LocalDateTime.now()).build();

        // Quando eu tentar salvar qualquer coisa, retorno o producer
        // Pois eu recebo no parametro apenas o nome do producer
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producer);

        // Troco de .get para .post
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/producers")
                        // O meu conteudo (requestBody) é minha requisicao
                        .content(request)
                        // No meu controller eu estou passando um header, entao preciso coloca-lo aqui (o value é apenas para testes)
                        .header("x-api-key", "v1")
                        // O tipo de conteudo recebido é um json
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                // Não é necessario, mas é uma forma de forçar a receber e enviar um json
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/producers update a producer")
    @Order(7)
    void update_updateProducer_WhenSucessFull() throws Exception {
        var request = readResourceFile("producer/put-request-producer-200.json");

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
//                Não retorna nada de json, é um metodo sem retorno
//                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers throws ResponseStatusException when producer is not found")
    @Order(8)
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
        var request = readResourceFile("producer/put-request-producer-404.json");
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not Found"));
    }

    @Test
    @DisplayName("DELETE v1/producers removes producer by id")
    @Order(9)
    void deleteById_removesProducer_WhenSucessFull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = producerList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers throws ResponseStatusException when producer is not found")
    @Order(10)
    void deleteById_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
        var id = 1000000L;
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not Found"));
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}