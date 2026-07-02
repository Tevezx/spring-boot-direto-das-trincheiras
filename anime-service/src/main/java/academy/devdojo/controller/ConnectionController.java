package academy.devdojo.controller;

import external.dependency.Connection;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/connections")
@AllArgsConstructor
public class ConnectionController {
    
    private Connection connection;

    @GetMapping()
    public ResponseEntity<Connection> getConnection() {
        return ResponseEntity.ok().body(connection);
    }
}
