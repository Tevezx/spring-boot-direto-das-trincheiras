package academy.devdojo.mapper;

import academy.devdojo.domain.User;
import academy.devdojo.request.RequestPostUser;
import academy.devdojo.request.RequestPutUser;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    List<UserGetResponse> toUserListGetResponse(List<User> userList);
    UserGetResponse toUserGetResponse(User user);

    User toUserPostRequest(RequestPostUser requestPostUser);
    UserPostResponse toUserPostResponse(User user);

    User toUserPutResponse(RequestPutUser requestPutUser);
}
