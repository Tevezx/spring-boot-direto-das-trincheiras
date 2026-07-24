package academy.devdojo.controller;

import academy.devdojo.mapper.UserProfileMapper;
import academy.devdojo.response.UserProfileGetResponse;
import academy.devdojo.response.UserProfileUserGetResponse;
import academy.devdojo.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping()
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        log.debug("Request received to list all user profiles");
        var userProfiles = service.findAll();
        var userProfileResponse = mapper.toUserProfileGetResponse(userProfiles);
        return ResponseEntity.ok(userProfileResponse);
    }

    @GetMapping("/profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findById(@PathVariable Long id) {
        log.debug("Request received to list all users by profile id '{}'", id);
        var usersByProfileId = service.findAllUsersByProfileId(id);
        var userProfileUserResponse = mapper.toUserProfileUserGetResponse(usersByProfileId);

        return ResponseEntity.ok(userProfileUserResponse);
    }
}
