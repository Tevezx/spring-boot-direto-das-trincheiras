package academy.devdojo.config;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@Profile("itest") // Dessa forma, o test container so sera executado quando o profile "itest" for chamado
// CGlib vs JDK Proxy | ADP
public class TestcontainersConfiguration {
    // Se voce for criar um containerMysql, utilize esse ServiceConnection (Isso que significa o @Bean)

    @Bean
    @ServiceConnection
    // @RestartScope possibilita que seja criado apenas um container do mysql para realizar os testes
    // ao inves de criar um container para cada um dos testes
    @RestartScope
    MySQLContainer mysqlContainer() {
        return new MySQLContainer(DockerImageName.parse("mysql:9.0.1"));
    }
}
