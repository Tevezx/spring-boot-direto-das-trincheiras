package academy.devdojo.mapper;

import academy.devdojo.domain.Producer;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    // sem o mapping, ele deixa o createdAt como nulo, mas com ele é possivel identificar o atributo e dizer o valor
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000L))")
    Producer toProducer(ProducerPostRequest postRequest);

    // primeiro é quem desejamos retornar
    ProducerGetResponse toProducerGetResponse(Producer producer);
}
