package academy.devdojo.mapper;

import academy.devdojo.domain.Anime;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AnimeMapper {
    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000L))")
    Anime toAnimePostRequest(AnimePostRequest animePostRequest);
    AnimePostResponse toAnimePostResponse(Anime anime);

    // Update
    Anime toAnime (AnimePutRequest animePutRequest);

    AnimeGetResponse toAnimeGetResponse(Anime anime);
    List<AnimeGetResponse> toAnimeGetResonseList(List<Anime> animes);
}
