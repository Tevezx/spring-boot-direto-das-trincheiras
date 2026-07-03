package academy.devdojo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Realiza a auto configuracao para poder utilizar no application.yaml
// Porém, só funciona com o intelij ultimate
// Todas as propriedades vao comecar com database.algo
@ConfigurationProperties(prefix = "database")
public record ConnectionConfigurationProperties(String url, String username, String password) {

}
