package academy.devdojo.mapper;

import academy.devdojo.domain.Producer;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {
    // Podemos remover a linha de baixo, pois agora estou passando no meu mapper e identificando a interface como um component
    // ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    // sem o mapping, ele deixa o createdAt como nulo, mas com ele é possivel identificar o atributo e dizer o valor
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000L))")
    Producer toProducerPostRequest(ProducerPostRequest postRequest);

    ProducerPostResponse toProducerPostResponse(Producer producer);

    Producer toProducerUpdated(ProducerPutRequest producerPutRequest);

    // primeiro é quem desejamos retornar
    ProducerGetResponse toProducerGetResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

}
