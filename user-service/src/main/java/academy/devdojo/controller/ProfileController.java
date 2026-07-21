package academy.devdojo.controller;

import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.request.RequestPostProfile;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;

    @GetMapping()
    public ResponseEntity<List<ProfileGetResponse>> findAll() {
        var profile = service.findAll();
        var profileGetResponse = mapper.toProfileGetResponseList(profile);

        return ResponseEntity.ok(profileGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfileGetResponse> findById(@PathVariable Long id) {
        var profile = service.findById(id);
        var profileGetResponse = mapper.toProfileGetResponse(profile);

        return ResponseEntity.ok(profileGetResponse);
    }

    @PostMapping()
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid RequestPostProfile requestPostProfile) {
        var toProfile = mapper.toProfile(requestPostProfile);
        var profileSaved = service.save(toProfile);

        var profilePostResponse = mapper.toProfilePostResponse(profileSaved);
        return ResponseEntity.status(201).body(profilePostResponse);
    }
}
