package academy.devdojo.controller;

import academy.devdojo.exception.ApiError;
import academy.devdojo.exception.DefaultErrorMessage;
import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.RequestPostUser;
import academy.devdojo.request.RequestPutUser;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Log4j2
// Tag -> anotação utilizada para o swagger, possibilita colocarmos um nome e descricao
@Tag(name = "User API", description = "User related endpoints")
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    // Produces diz ao swagger que é obrigado a utilizar content type json no media type, demonstrando que é um json
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    // Adicionando um sumário e descricao a minha rota no swagger
    @Operation(summary = "Get all users", description = "Get all users available system",
            responses = {
                    // Descricao da resposta da rota no swagger, com o http status
                    @ApiResponse(description = "List all users",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserGetResponse.class)))
                    )
            })
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
    @Operation(summary = "Get user by id",
            responses = {
                    // Descricao da resposta da rota no swagger, com o http status
                    @ApiResponse(description = "Get user by id",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGetResponse.class))
                    ),
                    @ApiResponse(description = "User not found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            })
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("User find by id");
        var userById = service.findById(id);
        var userResponse = mapper.toUserGetResponse(userById);

        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping()
    // Dizendo que a resposta é um http status 201 ao swagger
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user",
            responses = {
                    // Descricao da resposta da rota no swagger, com o http status
                    @ApiResponse(description = "Save user in the database",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserPostResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))
                    )
            })
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
