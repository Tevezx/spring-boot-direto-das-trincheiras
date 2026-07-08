package academy.devdojo.controller;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.RequestPostUser;
import academy.devdojo.request.RequestPutUser;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping()
    public ResponseEntity<List<UserGetResponse>> findAll() {
        log.debug("Finding all users");
        var userFindAll = service.findAll(null);
        var userResponse = mapper.toUserListGetResponse(userFindAll);

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("filterName")
    public ResponseEntity<List<UserGetResponse>> listAllUserName(@RequestParam String name) {
        log.debug("List all users when name");
        var userListName = service.findAll(name);
        var userResponse = mapper.toUserListGetResponse(userListName);

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("User find by id");
        var userById = service.findById(id);
        var userResponse = mapper.toUserGetResponse(userById);

        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping()
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid RequestPostUser requestPostUser) {
        log.debug("Saving user");

        var user = mapper.toUserPostRequest(requestPostUser);
        var userSaved = service.save(user);

        var userPostResponse = mapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(201).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Deleted user by id");
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody @Valid RequestPutUser requestPutUser) {
        var userUpdate = mapper.toUserPutResponse(requestPutUser);
        service.update(userUpdate);

        return ResponseEntity.noContent().build();
    }
}
