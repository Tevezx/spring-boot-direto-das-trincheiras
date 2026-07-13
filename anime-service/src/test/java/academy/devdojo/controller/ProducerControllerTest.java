package academy.devdojo.controller;

// Serve para startar um servidor para poder realizar os testes (é mais utilizado em testes de integracao)
// Startando como se tivesse rodando minha aplicacao normal na porta mockada
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)

// Serve para poder fazer uma chamada rest
//@AutoConfigureMockMvc

import academy.devdojo.comons.FileUtils;
import academy.devdojo.comons.ProducerUtils;
import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Meio termo entre teste unitario e teste de integracao (slices test)
// Interessante utilizar quando voce quer que seja testes mais rapidos para testar o controller
@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
// Faz a mesma coisa que o @Import, so que ja importa todas as classes do meu pacote academy.devdojo
@ComponentScan(basePackages = {"academy.devdojo", "external.dependency"})
// Definindo um profile para testes
@ActiveProfiles("test")
class ProducerControllerTest {
    private static final String URL = "/v1/producers";
    @Autowired
    private MockMvc mockMvc;

    // Faz a mesma coisa que @Mock
    @MockitoBean // Ao invés de utilizar a lista do producerData, ele vai utilizar a lista do void init() mockada
    private ProducerData producerData;

    // estou mockando apenas um determinado metodo, no caso: save
    @MockitoSpyBean
    private ProducerHardCodedRepository repository;
    private List<Producer> producerList = new ArrayList<>();

    // Carregar um arquivo
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProducerUtils producerUtils;

    // esse metodo vai ser executado primeiro do que qualquer outro metodo
    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("GET v1/producers returns to list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenNameIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        // Digo qual o json que eu espero como resposta da requisicao
        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok e leita meu arquivo response
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
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
        var response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
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
        var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
        // Esse nome nao existe na lista, entao é para retornar a lista vazia segundo o json que eu defini em cima
        var name = "Sony";

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
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
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-1-200.json");
        // Esse nome nao existe na lista, entao é para retornar a lista vazia segundo o json que eu defini em cima
        var id = 1L;

        // mockMvc faça um get para essa rota e imprima esse cara e espera o status ok
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/90 throws ThrowNotFound 404 when producer is not found")
    @Order(5)
    void findById_ThrowsThrowNotFound_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = fileUtils.readResourceFile("producer/get-producer-by-id-404.json");
        var id = 90L;

        // verificando se o status 404 esta sendo retornado pelo isNotFound e dizendo qual mensagem deve aparecer no body
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_createsProducer_WhenSucessFull() throws Exception {
        var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");
        // Eu faço dessa forma esse metodo por conta do createdAt
        var producer = Producer.builder().id(99L).name("Sony").createdAt(LocalDateTime.now()).build();

        // Quando eu tentar salvar qualquer coisa, retorno o producer
        // Pois eu recebo no parametro apenas o nome do producer
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producer);

        // Troco de .get para .post
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
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
        var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
//                Não retorna nada de json, é um metodo sem retorno
//                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers throws ThrowNotFound when producer is not found")
    @Order(8)
    void update_ThrowsThrowNotFound_WhenProducerIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");
        var response = fileUtils.readResourceFile("producer/put-producer-by-id-404.json");
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/producers removes producer by id")
    @Order(9)
    void deleteById_removesProducer_WhenSucessFull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = producerList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers throws ThrowNotFound when producer is not found")
    @Order(10)
    void deleteById_ThrowsThrowNotFound_WhenProducerIsNotFound() throws Exception {
        var id = 1000000L;
        var response = fileUtils.readResourceFile("producer/delete-producer-by-id-404.json");
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/producers returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        // O meu conteudo (requestBody) é minha requisicao
                        .content(request)
                        // No meu controller eu estou passando um header, entao preciso coloca-lo aqui (o value é apenas para testes)
                        .header("x-api-key", "v1")
                        // O tipo de conteudo recebido é um json
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/producers returns bad request when fields are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-producer-empty-field-400.json", allRequiredErrors()),
                Arguments.of("post-request-producer-blank-field-400.json", allRequiredErrors())
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-producer-empty-field-400.json", allRequiredErrors()),
                Arguments.of("put-request-producer-blank-field-400.json", allRequiredErrors()),
                Arguments.of("put-request-producer-id-null-400.json", errorIdCannotNull())
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";
        return List.of(nameRequiredError);
    }

    private static List<String> errorIdCannotNull() {
        var idCannotNullError = "The field 'id' cannot be null";
        return List.of(idCannotNullError);
    }
}