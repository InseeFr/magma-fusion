package fr.insee.rmes.magmafusion.api.testcontainers.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@AutoConfigureRestTestClient
@Slf4j
@TestPropertySource(properties = "spring.profiles.active=security.disabled")
public class TestContainer {
    static GraphDBContainer container = new GraphDBContainer("ontotext/graphdb:10.8.8").withReuse(false);

    @Autowired
    protected RestTestClient restTestClient;

    @BeforeAll
    static void startContainer(){
        container.start();

    }


    @DynamicPropertySource
    static void overrideSpringProperties(DynamicPropertyRegistry registry) {
        String url  = "http://" + container.getHost() + ":" + container.getMappedPort(7200)+ "/repositories/magmafusion";
        log.info("Graphdb URL: " + url);
        registry.add("fr.insee.rmes.magmafusion.api.sparqlEndpoint", () -> url) ;
    }

    protected String loadExpectedJson(String resourceName) throws IOException {
        return new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/" + resourceName))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    protected String bodyAsString(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }

}
